package com.jj.routingservice.util

import android.content.Context
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileHelper {
    var cachePath : String = ""
    var settingPath : String = ""

    fun init(context:Context){
        val path = getDiskCacheDir(context)
        settingPath = "$path/setting"
        cachePath = "$path/cache"
        checkFile(settingPath)
        checkFile(cachePath)
    }
    private fun checkFile(path:String){
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
    }
    fun moveFile(srcFileName: String, destDir: String): Boolean {
        return moveFile(srcFileName, destDir, "")
    }
    fun getDiskCacheDir(context: Context): String {
        var cachePath = context.externalCacheDir
        if (cachePath == null) {
            cachePath = context.cacheDir
        }
        try {
            return cachePath!!.absolutePath
        } catch (e: Exception) {
        }
        return ""
    }
    /**
     * 移动文件
     *
     * @param srcFileName 源文件完整路径
     * @param destDir 目的目录完整路径
     * @param name 文件名
     * @return 文件移动成功返回true，否则返回false
     */
    private fun moveFile(srcFileName: String, destDir: String, name: String?): Boolean {
        val srcFile = File(srcFileName)
        if (!srcFile.exists() || !srcFile.isFile) return false
        val destDirFile = File(destDir)
        if (!destDirFile.exists()) destDirFile.mkdirs()
        return srcFile.renameTo(File(destDir + File.separator + (if (TextUtils.isEmpty(name)) srcFile.name else name)))
    }
    fun getNameOfUrl(url: String): String {
        return getNameOfUrl(url, "")
    }

    fun getNameOfUrl(url: String, defaultName: String): String {
        var name = ""
        val pos = url.lastIndexOf('/')
        if (pos >= 0) name = url.substring(pos + 1)

        if (TextUtils.isEmpty(name)) name = defaultName

        return name
    }
    fun writeFile(fileName: String?, content: String): Boolean {
        var res = false
        try {
            val fout = FileOutputStream(fileName)
            fout.write(content.toByteArray(), 0, content.toByteArray().size)
            fout.flush()
            fout.close()
            res = true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return res
    }
    fun readFile(fileName: String?): String {
        var res = ""
        try {
            val fin = FileInputStream(fileName)
            // FileInputStream fin = openFileInput(fileName);
            // 用这个就不行了，必须用FileInputStream
            val length = fin.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            res = String(buffer, charset("UTF-8"))
            fin.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (res.startsWith("\ufeff")) {
            res = res.substring(1)
        }
        return res
    }

}