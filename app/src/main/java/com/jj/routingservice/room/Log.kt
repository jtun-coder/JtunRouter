package com.jj.routingservice.room

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Log(val type:Int,
               val tag:String,
               val msg:String,
               val time:Long,
               @PrimaryKey(autoGenerate = true)
               val id:Long = 0,){
    companion object{
       const val MAX_ITEMS = 20000
    }
    @androidx.room.Dao
    abstract class Dao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        protected abstract suspend fun updateInternal(value: Log): Long
        suspend fun update(value: Log) {
            //判断是否超过指定数量，如果超过先删除历史的
            deleteMaxLog(MAX_ITEMS)
            val result = updateInternal(value)
        }
        @Query("SELECT * FROM `Log` order by time desc limit :limit offset :offset")
        protected abstract suspend fun selectList(limit: Int, offset: Int): List<Log>
        suspend fun getList(limit: Int, offset: Int) = selectList(limit,offset)

        @Query("SELECT * FROM `Log` order by time desc")
        protected abstract suspend fun selectList(): List<Log>
        suspend fun getList() = selectList()

        @Query("SELECT * FROM `Log` WHERE type=:type order by time desc limit :limit offset :offset")
        protected abstract suspend fun selectList(type: Int, limit: Int, offset: Int): List<Log>
        suspend fun getList(type: Int, limit: Int, offset: Int) = selectList(type,limit,offset)

        @Query("SELECT * FROM `Log` WHERE type=:type AND id > :id order by time desc limit :limit")
        abstract suspend fun refreshListById(type: Int, limit: Int, id: Long): List<Log>

        @Query("SELECT * FROM `Log` WHERE type=:type AND id < :id order by time desc limit :limit")
        abstract suspend fun moreListById(type: Int, limit: Int, id: Long): List<Log>

        /**
         * 删除超过最大数量的日志
         */
        @Query("delete from `Log` where (select count(id) from `Log`) > :max and id in (select id from `Log` order by time desc limit (select count(id) from `Log`) offset :max )")
        protected abstract suspend fun deleteMaxLog(max:Int)
    }
}