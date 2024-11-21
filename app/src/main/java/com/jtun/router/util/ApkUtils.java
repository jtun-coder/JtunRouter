package com.jtun.router.util;

import static android.content.Context.ACTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ApkUtils {
    /**
     * 栈顶包名获取总类
     *
     * @param context
     * @return
     */
    public static String getTopPackageName(Context context) {
        String mTopPackname = null;// 当前居于栈顶部的报名

        if (Build.VERSION.SDK_INT < 20) {// 如果是android 5.0及以下设备
            // 获取一个Activity的管理器，ActivityManager可以动态的观察到当前存在哪些进程。
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            // 获取到当前正在栈顶运行的Activity。
            ActivityManager.RunningTaskInfo taskinfo = am.getRunningTasks(1)
                    .get(0);
            // 获取到当前任务栈顶程序所对应的包名。
            mTopPackname = taskinfo.topActivity.getPackageName();

        } else if (Build.VERSION.SDK_INT >= 20) {
            mTopPackname = getCurrentPkgName(context);
            if ("Currentpkg".equals(mTopPackname)) {
                mTopPackname = getTaskPackageName(context);
            }
        }
        return mTopPackname;
    }

    /**
     * 获取最顶层程序包名(Android版本在5.1.1以上使用)
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NewApi")
    public static String getTaskPackageName(Context context) {
        String currentApp = "CurrentNULL";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context
                    .getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && !appList.isEmpty()) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey())
                            .getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) context
                    .getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am
                    .getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
    }

    /**
     * 获取最顶层程序包名(Android版本在5.0以上使用)
     *
     * @return
     */
    public static String getCurrentPkgName(Context context) {
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        int START_TASK_TO_FRONT = 2;
        String pkgName = "Currentpkg";
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Integer state = null;
                try {
                    state = field.getInt(app);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (state != null && state == START_TASK_TO_FRONT) {
                    currentInfo = app;
                    break;
                }
            }
        }
        if (currentInfo != null) {
            pkgName = currentInfo.processName;
        }
        return pkgName;
    }
    /**
     * 根据包名启动某一个应用
     *
     * @param context
     * @param cn
     */
    public static void startAct(Context context, ComponentName cn) throws Exception{
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cn);
        context.startActivity(intent);
    }

    /**
     * 关闭指定包名应用
     * @param context 上下文
     * @param packagename 包名
     * @return
     */
    public static boolean killPackageProcesses(Context context, String packagename) {
        boolean result;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
//            am.killBackgroundProcesses(packagename);
//            Process.killProcess(findPIDbyPackageName(context,packagename));
            try {
                Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
                method.invoke(am, packagename); //packageName是需要强制停止的应用程序包名
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = !isPackageRunning(context, packagename);
        } else {
            result = false;
        }
        KLog.i("killPackageProcesses : " + packagename + "----" + result);
        return result;
    }
    public static boolean cmdKillApp(String pn){
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream out =  process.getOutputStream();
            String cmd = "am force-stop " + pn + " \n";
            out.write(cmd.getBytes());
            out.flush();
            out.close();

            var fis=process.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止 目的就是要阻塞当前的线程到命令结束的时间
            while((line=br.readLine())!=null)
            {
                KLog.e(line);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 根据报名查找指定pid
     * @param context 上下文
     * @param packagename 包名
     * @return pid
     */
    public static int findPIDbyPackageName(Context context, String packagename) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int result = -1;
        if (am != null) {
            for (ActivityManager.RunningAppProcessInfo pi : am.getRunningAppProcesses()) {
                if (pi.processName.equalsIgnoreCase(packagename)) {
                    result = pi.pid;
                }
                if (result != -1) break;
            }
        } else {
            result = -1;
        }

        return result;
    }

    /**
     * 根据报名返回该进程是否启动
     * @param context 上下文
     * @param packagename 包名
     * @return
     */
    public static boolean isPackageRunning(Context context, String packagename) {
        return findPIDbyPackageName(context, packagename) != -1;
    }
}
