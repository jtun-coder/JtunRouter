package com.jtun.router.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jtun.router.util.KLog

@androidx.room.Dao
abstract class ClientDao {
    @Query("SELECT * FROM `ClientConnected` WHERE `mac` = :mac")
    protected abstract suspend fun lookup(mac: String): ClientConnected?
    suspend fun lookupOrDefault(mac: String) = lookup(mac) ?: ClientConnected(mac)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun updateInternal(value: ClientConnected): Long
    suspend fun update(value: ClientConnected) {
        val result = updateInternal(value)
        KLog.i("updateInternal result : $result")
    }

    @Query("SELECT * FROM `ClientConnected`")
    protected abstract suspend fun selectList(): List<ClientConnected>
    suspend fun getList() = selectList()

    @Query("DELETE FROM `ClientConnected` WHERE `mac` = :mac")
    protected abstract suspend fun deleteClient(mac: String)
    suspend fun deleteClientMac(mac: String) {
        deleteClient(mac)
    }

    @Transaction
    open suspend fun upsert(mac: String, operation: suspend ClientConnected.() -> Unit) = lookupOrDefault(mac).apply {
        operation()
        update(this)
    }
}