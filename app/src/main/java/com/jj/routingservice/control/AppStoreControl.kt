package com.jj.routingservice.control

import android.annotation.SuppressLint
import android.text.TextUtils
import com.dolphin.localsocket.server.LocalServer
import com.jj.routingservice.App
import com.jj.routingservice.http.ApiService
import com.jj.routingservice.http.NetworkUtil
import com.jj.routingservice.http.download.DownLoadSubscriber
import com.jj.routingservice.http.download.DownloadApkCallBack
import com.jj.routingservice.http.download.ProgressCallBack
import com.jj.routingservice.http.interceptor.ProgressInterceptor
import com.jj.routingservice.room.AppDatabase
import com.jj.routingservice.room.AppInfo
import com.jj.routingservice.util.FileHelper
import com.jj.routingservice.util.JLog
import com.jj.routingservice.util.KLog
import com.jj.routingservice.util.SystemCtrlUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.util.UUID
import java.util.WeakHashMap
import java.util.concurrent.TimeUnit

/**
 * 应用商城相关管理
 * 1.安装指定应用
 * 2.卸载指定应用
 * 3.维护下载、安装进度任务
 */
class AppStoreControl private constructor(){
    private val tag = AppStoreControl::class.java.simpleName
    private val apkList :WeakHashMap<String,ApkStats> = WeakHashMap()
    private var retrofit: Retrofit? = null
    init {
        buildNetWork()
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: AppStoreControl? = null
        fun getInstance(): AppStoreControl {
            return instance ?: synchronized(this) {
                instance ?: AppStoreControl().also { instance = it }
            }
        }
    }

    /**
     * 获取安装进度信息
     */
    fun getInstallInfo(packageName: String) : ApkStats?{
        return apkList[packageName]
    }

    /**
     * 开始安装指定Apk
     */
    fun createInstallApk(url:String,appInfo:AppInfo) : ApkStats{
        //1.判断当前url是否有正在下载的
        val info = apkList[appInfo.packageName]
        if(info != null){
            return info
        }
        val apkStats = ApkStats(UUID.randomUUID().toString(),url,0,0,0,appInfo)
        apkList[appInfo.packageName] = apkStats
        //2.返回uuid给客户端
        //3.执行下载、安装
        downloadApk(url,appInfo.packageName)
        return apkStats
    }
    /**
     * 卸载指定应用
     */
    fun unInstallApk(packageName:String):Boolean{
        val result = SystemCtrlUtil.rootSlienceUninstallApk(packageName)
        if(result){
            apkList.remove(packageName)
        }
        App.app.applicationScope.launch {
            LocalServer.instance.killClient(packageName)
            AppDatabase.instance.appDao.deleteApp(packageName)
        }
        return result
    }
    private fun downloadApk(url: String,packageName: String) {
        val name: String = FileHelper.getNameOfUrl(url, "JJMifi.apk")
        val appStats = apkList[packageName]
        appStats?.status = 1
        load(url, object : DownloadApkCallBack<String>(FileHelper.cachePath, FileHelper.settingPath, name) {
            override fun onSuccess(filePath: String?) {
                KLog.i("filePath: $filePath")
                JLog.t(tag,"$packageName download success")
                if (!TextUtils.isEmpty(filePath)) {
                    appStats?.status = 3
                    val result = SystemCtrlUtil.rootSlienceInstallApk(filePath)
                    JLog.t(tag,"$packageName Install $result")
                    appStats?.status = if (result) 4 else 5
                    //4.成功后添加app信息到数据库
                    if(appStats?.status == 4){
                        appStats.appInfo.isRun = false
                        GlobalScope.launch {
                            AppDatabase.instance.appDao.update(appStats.appInfo)
                        }
                    }
                }
            }

            override fun progress(progress: Long, total: Long, tag: String?) {
                appStats?.progress = progress
                appStats?.total = total
            }

            override fun onError(e: Throwable?) {
                JLog.e(tag,"$packageName download error ${e?.message}")
                appStats?.status = 5
            }
        })
    }

    //下载
    private fun load(downUrl: String, callBack: ProgressCallBack<*>) {
        retrofit?.let {
            it.create(ApiService::class.java)
            .download(downUrl)
            .subscribeOn(Schedulers.io()) //请求网络 在调度者的io线程
            .observeOn(Schedulers.io()) //指定线程保存文件
            .doOnNext { responseBody -> callBack.saveFile(responseBody) }
            .observeOn(AndroidSchedulers.mainThread()) //在主线程中更新ui
            .subscribe(DownLoadSubscriber(callBack))
        }
    }

    private fun buildNetWork() {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ProgressInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(NetworkUtil.url)
            .build()
    }

    /**
     * status 0未下载 1下载中 2下载完成 3安装中 4 安装完成 5失败
     */
    data class ApkStats(val uuid:String,val url:String,var status:Int,var total:Long,var progress:Long,var appInfo: AppInfo)
}