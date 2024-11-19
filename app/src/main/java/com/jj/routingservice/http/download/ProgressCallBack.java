package com.jj.routingservice.http.download;

import android.util.Log;


import com.jj.routingservice.bus.RxBus;
import com.jj.routingservice.bus.RxSubscriptions;
import com.jj.routingservice.util.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;


/**
 * Created by goldze on 2017/9/26 0026.
 */

public abstract class ProgressCallBack<T> {

    protected String destFileDir; // 本地文件存放路径
    protected String cacheDir; // 文件下载缓存目录
    protected String destFileName; // 文件名
    private Disposable mSubscription;
    private String filePath;//下载路径

    public ProgressCallBack(String cacheDir, String destFileDir, String destFileName) {
        this.cacheDir = cacheDir;
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();
    }

    public abstract void onSuccess(T t);

    public abstract void progress(long progress, long total,String tag);

    public String getFilePath() {
        return filePath;
    }

    protected void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void onStart() {
    }

    public void onCompleted() {
    }

    public abstract void onError(Throwable e);

    public void saveFile(ResponseBody body) {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            File dir = new File(cacheDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            if(cacheDir!= null &&!cacheDir.equals(destFileDir)) {
                FileHelper.INSTANCE.moveFile(file.getAbsolutePath(), destFileDir);
            }
            filePath = destFileDir + "/" + destFileName;
            unsubscribe();
            //onCompleted();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e("saveFile", e.getMessage());
            }
        }
    }

    /**
     * 订阅加载的进度条
     */
    public void subscribeLoadProgress() {
        mSubscription = RxBus.getDefault().toObservable(DownLoadStateBean.class)
                .observeOn(AndroidSchedulers.mainThread()) //回调到主线程更新UI
                .subscribe(new Consumer<DownLoadStateBean>() {
                    @Override
                    public void accept(DownLoadStateBean downLoadStateBean) throws Throwable {
                        progress(downLoadStateBean.getBytesLoaded(), downLoadStateBean.getTotal(),downLoadStateBean.getTag());
                    }
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    public void unsubscribe() {
        RxSubscriptions.remove(mSubscription);
    }
}