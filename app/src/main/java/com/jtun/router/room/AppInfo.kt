package com.jtun.router.room

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.dolphin.localsocket.bean.PackageInfo
import com.jtun.router.util.KLog

@Entity
data class AppInfo(@PrimaryKey
                   val packageName:String,
                   val canonicalName :String?,
                   val versionCode:Long,
                   val versionName : String,
                   val name:String,
                   val brief:String,
                   var isRun :Boolean = true){
    companion object{
        fun AppInfo.toCompat() = PackageInfo(packageName,canonicalName,versionCode,versionName,name,brief,isRun)
        fun PackageInfo.toCompat() = AppInfo(packageName,canonicalName,versionCode,versionName,name,brief,isRun)
    }

    @androidx.room.Dao
    abstract class Dao {
        @Query("SELECT * FROM `AppInfo` WHERE `packageName` = :packageName")
        protected abstract suspend fun lookup(packageName: String): AppInfo?
        suspend fun lookupOrDefault(packageName: String) = lookup(packageName)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        protected abstract suspend fun updateInternal(value: AppInfo): Long
        suspend fun update(value: AppInfo) {
            val result = updateInternal(value)
            KLog.i("updateInternal result : $result")
        }

        @Query("SELECT * FROM `AppInfo`")
        protected abstract suspend fun selectList(): List<AppInfo>
        suspend fun getList() = selectList()

        @Query("DELETE FROM `AppInfo` WHERE `packageName` = :packageName")
        protected abstract suspend fun delete(packageName: String)
        suspend fun deleteApp(packageName: String) {
            delete(packageName)
        }
    }
}

