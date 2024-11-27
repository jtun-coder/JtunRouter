package com.jtun.router

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipboardManager
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import com.jtun.router.control.WifiApControl
import com.jtun.router.net.DhcpWorkaround
import com.jtun.router.room.AppDatabase
import com.jtun.router.util.DeviceStorageApp
import com.jtun.router.util.KLog
import com.jtun.router.util.Services
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class App : Application() {
    lateinit var deviceStorage: Application
    val pref by lazy { PreferenceManager.getDefaultSharedPreferences(deviceStorage) }
    val clipboard by lazy { getSystemService<ClipboardManager>()!! }
    val location by lazy { getSystemService<LocationManager>() }
    val hasTouch by lazy { packageManager.hasSystemFeature("android.hardware.faketouch") }
    val customTabsIntent by lazy {
        CustomTabsIntent.Builder().apply {
            setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, CustomTabColorSchemeParams.Builder().apply {
                setToolbarColor(resources.getColor(R.color.light_colorPrimary, theme))
            }.build())
            setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, CustomTabColorSchemeParams.Builder().apply {
                setToolbarColor(resources.getColor(R.color.dark_colorPrimary, theme))
            }.build())
        }.build()
    }
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var app: App
    }
    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
        app = this
        hookWebView()
        deviceStorage = DeviceStorageApp(this)
        // alternative to PreferenceManager.getDefaultSharedPreferencesName(this)
        deviceStorage.moveSharedPreferencesFrom(this, PreferenceManager(this@App).sharedPreferencesName)
        deviceStorage.moveDatabaseFrom(this, AppDatabase.DB_NAME)
        Services.init { this }
        WifiApControl.getInstance().init(this)
        KLog.init(BuildConfig.DEBUG)
        BootReceiver.migrateIfNecessary()
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
        ServiceNotification.updateNotificationChannels()
        if (DhcpWorkaround.shouldEnable) DhcpWorkaround.enable(true)

    }

    fun logEvent(event: String){
        KLog.i(event)
    }


    fun reboot(){
        applicationScope.launch {
            delay(1000)
            val reboot = Intent(Intent.ACTION_REBOOT);
            reboot.putExtra("nowait", 1);
            reboot.putExtra("interval", 1);
            reboot.putExtra("window", 0);
            sendBroadcast(reboot)
        }

    }
    fun shutdown(){
        applicationScope.launch {
            delay(1000)
            try {
                val pManager = getSystemService(POWER_SERVICE)
                if (pManager != null) {
                    val method = pManager.javaClass.getMethod("shutdown", Boolean::class.javaPrimitiveType, String::class.java, Boolean::class.javaPrimitiveType)
                    method.invoke(pManager, false, null, false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    //发送恢复出厂设置的广播事件
    fun masterClear() {
        sendBroadcast(Intent("android.intent.action.MASTER_CLEAR"))
    }

    /**
     * 给WebViewFactory sProviderInstance赋值，避免进行进程判断而抛出异常（系统应用不能使用WebView）
     */
    fun hookWebView() {
        val sdkInt = Build.VERSION.SDK_INT
        try {
            val factoryClass = Class.forName("android.webkit.WebViewFactory")
            val field = factoryClass.getDeclaredField("sProviderInstance")
            field.isAccessible = true
            var sProviderInstance = field[null]
            if (sProviderInstance != null) {
                KLog.i("sProviderInstance isn't null")
                return
            }
            val getProviderClassMethod = if (sdkInt > 22) {
                factoryClass.getDeclaredMethod("getProviderClass")
            } else if (sdkInt == 22) {
                factoryClass.getDeclaredMethod("getFactoryClass")
            } else {
                KLog.i("Don't need to Hook WebView")
                return
            }
            getProviderClassMethod.isAccessible = true
            val factoryProviderClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
            val delegateClass = Class.forName("android.webkit.WebViewDelegate")
            val delegateConstructor = delegateClass.getDeclaredConstructor()
            delegateConstructor.isAccessible = true
            if (sdkInt < 26) { //低于Android O版本
                val providerConstructor = factoryProviderClass.getConstructor(delegateClass)
                if (providerConstructor != null) {
                    providerConstructor.isAccessible = true
                    sProviderInstance = providerConstructor.newInstance(delegateConstructor.newInstance())
                }
            } else {
                val chromiumMethodName = factoryClass.getDeclaredField("CHROMIUM_WEBVIEW_FACTORY_METHOD")
                chromiumMethodName.isAccessible = true
                var chromiumMethodNameStr = chromiumMethodName[null] as String
                if (chromiumMethodNameStr == null) {
                    chromiumMethodNameStr = "create"
                }
                val staticFactory = factoryProviderClass.getMethod(chromiumMethodNameStr, delegateClass)
                if (staticFactory != null) {
                    sProviderInstance = staticFactory.invoke(null, delegateConstructor.newInstance())
                }
            }

            if (sProviderInstance != null) {
                field["sProviderInstance"] = sProviderInstance
                KLog.i("Hook success!")
            } else {
                KLog.i("Hook failed!")
            }
        } catch (e: Throwable) {
            KLog.w(e)
        }
    }

}