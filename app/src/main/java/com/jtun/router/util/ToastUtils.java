package com.jtun.router.util;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.jtun.router.App;

import java.lang.ref.WeakReference;

/**
 * Created by goldze on 2017/5/14.
 * 吐司工具类
 */
public final class ToastUtils {

    private static final int DEFAULT_COLOR = 0x12000000;
    private static Toast sToast;
    private static int gravity         = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private static int xOffset         = 0;
    private static int yOffset         = (int) (64 * App.app.getResources().getDisplayMetrics().density + 0.5);
    private static int backgroundColor = DEFAULT_COLOR;
    private static int bgResource      = -1;
    private static int messageColor    = DEFAULT_COLOR;
    private static WeakReference<View> sViewWeakReference;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置吐司位置
     *
     * @param gravity 位置
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        ToastUtils.gravity = gravity;
        ToastUtils.xOffset = xOffset;
        ToastUtils.yOffset = yOffset;
    }

    /**
     * 设置吐司view
     *
     * @param layoutId 视图
     */
    public static void setView(@LayoutRes int layoutId) {
        LayoutInflater inflate = (LayoutInflater) App.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sViewWeakReference = new WeakReference<>(inflate.inflate(layoutId, null));
    }

    /**
     * 设置吐司view
     *
     * @param view 视图
     */
    public static void setView(@Nullable View view) {
        sViewWeakReference = view == null ? null : new WeakReference<>(view);
    }

    /**
     * 获取吐司view
     *
     * @return view
     */
    public static View getView() {
        if (sViewWeakReference != null) {
            final View view = sViewWeakReference.get();
            if (view != null) {
                return view;
            }
        }
        if (sToast != null) return sToast.getView();
        return null;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景色
     */
    public static void setBackgroundColor(@ColorInt int backgroundColor) {
        ToastUtils.backgroundColor = backgroundColor;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    public static void setBgResource(@DrawableRes int bgResource) {
        ToastUtils.bgResource = bgResource;
    }

    /**
     * 设置消息颜色
     *
     * @param messageColor 颜色
     */
    public static void setMessageColor(@ColorInt int messageColor) {
        ToastUtils.messageColor = messageColor;
    }
    private static boolean isMainThread(){
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortSafe(final CharSequence text) {
        if(isMainThread()){
            show(text, Toast.LENGTH_SHORT);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortSafe(final @StringRes int resId) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_SHORT);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShortSafe(final @StringRes int resId, final Object... args) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_SHORT, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT, args);
                }
            });
        }

    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShortSafe(final String format, final Object... args) {
        if(isMainThread()){
            show(format, Toast.LENGTH_SHORT, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_SHORT, args);
                }
            });
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongSafe(final CharSequence text) {
        if(isMainThread()){
            show(text, Toast.LENGTH_LONG);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongSafe(final @StringRes int resId) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_LONG);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLongSafe(final @StringRes int resId, final Object... args) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_LONG, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_LONG, args);
                }
            });
        }
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLongSafe(final String format, final Object... args) {
        if(isMainThread()){
            show(format, Toast.LENGTH_LONG, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_LONG, args);
                }
            });
        }
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(CharSequence text) {
        if(isMainThread()){
            show(text, Toast.LENGTH_SHORT);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShort(@StringRes int resId) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_SHORT);
        }else{
            sHandler.post(() -> show(resId, Toast.LENGTH_SHORT));
        }
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShort(@StringRes int resId, Object... args) {
        if(isMainThread()){
            show(resId, Toast.LENGTH_SHORT,args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT,args);
                }
            });
        }
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(String format, Object... args) {
        if(TextUtils.isEmpty(format))
            return;
        if(isMainThread()){
            show(format, Toast.LENGTH_SHORT, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_SHORT, args);
                }
            });
        }
    }

    /**
     * 显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLong(String format, Object... args) {
        if(isMainThread()){
            show(format, Toast.LENGTH_LONG, args);
        }else{
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(format, Toast.LENGTH_LONG, args);
                }
            });
        }
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@StringRes int resId, int duration) {
        show(App.app.getString(resId), duration);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(@StringRes int resId, int duration, Object... args) {
        show(String.format(App.app.getResources().getString(resId), args), duration);
    }

    /**
     * 显示吐司
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(CharSequence text, int duration) {
        cancel();
        boolean isCustom = false;
        if (sViewWeakReference != null) {
            final View view = sViewWeakReference.get();
            if (view != null) {
                sToast = new Toast(App.app);
                sToast.setView(view);
                sToast.setDuration(duration);
                isCustom = true;
            }
        }
        if (!isCustom) {
            if (messageColor != DEFAULT_COLOR) {
                SpannableString spannableString = new SpannableString(text);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
                spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sToast = Toast.makeText(App.app, spannableString, duration);
            } else {
                sToast = Toast.makeText(App.app, text, duration);
            }
        }
        View view = sToast.getView();
        if (bgResource != -1) {
            view.setBackgroundResource(bgResource);
        } else if (backgroundColor != DEFAULT_COLOR) {
            view.setBackgroundColor(backgroundColor);
        }
        sToast.setGravity(gravity, xOffset, yOffset);
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}