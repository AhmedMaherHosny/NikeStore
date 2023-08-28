package com.example.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.local.db.entities.AddressEntity

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(address: AddressEntity)

    @Query("SELECT * FROM addresses LIMIT 1")
    suspend fun getAddress(): AddressEntity
}