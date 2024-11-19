package com.jj.routingservice.control

import android.annotation.SuppressLint
import android.text.TextUtils
import com.jj.routingservice.http.NetworkUtil
import com.jj.routingservice.http.download.DownLoadSubscriber
import com.jj.routingservice.http.download.DownloadApkCallBack
import com.jj.routingservice.http.download.ProgressCallBack
import com.jj.routingservice.http.interceptor.ProgressInterceptor
import com.jj.routingservice.util.FileHelper
import com.jj.routingservice.util.KLog
import com.jj.routingservice.util.SystemCtrlUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.util.concurrent.TimeUnit
class UpdateControl private constructor() {
    private var retrofit: Retrofit? = null

    init {
        buildNetWork()
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: UpdateControl? = null
        fun getInstance(): UpdateControl {
            return instance ?: synchronized(this) {
                instance ?: UpdateControl().also { instance = it }
            }
        }
    }


    fun downloadApk(url: String) {
        if(TextUtils.isEmpty(url))
            return
        val name: String = FileHelper.getNameOfUrl(url, "JJMifi.apk")
        load(url, object : DownloadApkCallBack<String>(FileHelper.cachePath, FileHelper.settingPath, name) {
            override fun onSuccess(filePath: String?) {
                KLog.i("filePath: $filePath")
                if (!TextUtils.isEmpty(filePath)) {
                    val result = SystemCtrlUtil.rootSlienceInstallApk(filePath)
                }
            }

            override fun progress(progress: Long, total: Long, tag: String?) {

            }

            override fun onError(e: Throwable?) {

            }
        })
    }
    fun downloadRom(url: String) {
        if(TextUtils.isEmpty(url))
            return
        val name: String = FileHelper.getNameOfUrl(url, "os.zip")
        load(url, object : DownloadApkCallBack<String>(FileHelper.cachePath, FileHelper.settingPath, name) {
            override fun onSuccess(filePath: String?) {
                KLog.i("filePath: $filePath")
                if (!TextUtils.isEmpty(filePath)) {
                    //执行系统更新
                }
            }

            override fun progress(progress: Long, total: Long, tag: String?) {

            }

            override fun onError(e: Throwable?) {

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

    private interface ApiService {
        @Streaming
        @GET
        fun download(@Url url: String): Observable<ResponseBody>
    }
}