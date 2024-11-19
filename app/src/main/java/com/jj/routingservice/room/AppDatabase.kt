package com.jj.routingservice.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jj.routingservice.App.Companion.app
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [ClientRecord::class,TrafficRecord::class,ClientConnected::class,AppInfo::class,Log::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "app.db"

        val instance by lazy {
            Room.databaseBuilder(app.deviceStorage, AppDatabase::class.java, DB_NAME).apply {
                fallbackToDestructiveMigration()
                setQueryExecutor { GlobalScope.launch { it.run() } }
            }.build()
        }
    }

    abstract val clientRecordDao: ClientRecord.Dao
    abstract val trafficRecordDao: TrafficRecord.Dao
    abstract val clientDao: ClientDao
    abstract val appDao : AppInfo.Dao
    abstract val logDao : Log.Dao
}
