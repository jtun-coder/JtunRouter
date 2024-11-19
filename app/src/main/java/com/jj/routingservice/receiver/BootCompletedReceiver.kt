package com.jj.routingservice.receiver

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.jj.routingservice.util.ApkUtils
import com.jj.routingservice.util.KLog

class BootCompletedReceiver : BroadcastReceiver() {
    private fun startAct(context: Context, cn: ComponentName) {
        val packageName: String = ApkUtils.getTopPackageName(context)
        val isRun = isPackageRunning(context, cn.packageName)
        KLog.i(cn.packageName + "  isRun : " + isRun)
        KLog.i("top package : $packageName")
        if (packageName == cn.packageName) {
            return
        }
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            intent.setComponent(cn)
            context.startActivity(intent)
        } catch (e: Exception) {
            KLog.i(e.message)
        }
    }

    /**
     * 根据报名返回该进程是否启动
     * @param context 上下文
     * @param packagename 包名
     * @return
     */
    private fun isPackageRunning(context: Context, packagename: String?): Boolean {
        return findPIDbyPackageName(context, packagename) != -1
    }

    /**
     * 根据报名查找指定pid
     * @param context 上下文
     * @param packagename 包名
     * @return pid
     */
    private fun findPIDbyPackageName(context: Context, packagename: String?): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var result = -1
        if (am != null) {
            for (pi in am.runningAppProcesses) {
                if (pi.processName.equals(packagename, ignoreCase = true)) {
                    result = pi.pid
                }
                if (result != -1) break
            }
        } else {
            result = -1
        }

        return result
    }

    override fun onReceive(context: Context, intent: Intent?) {
        KLog.i("接收到开机广播")
        /**
         * 主程序
         */
//        val pkgName = context.packageName
//        val clsName = "com.jj.routingservice" + ".MainActivity"
//        val cn = ComponentName(pkgName, clsName)
//        KLog.w(null, "ComponentName:$cn")
//        startAct(context, cn)

    }
}