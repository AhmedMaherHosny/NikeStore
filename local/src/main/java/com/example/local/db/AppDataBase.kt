package com.example.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.local.db.dao.AddressDao
import com.example.local.db.entities.AddressEntity

@Database(
    entities = [AddressEntity::class],
    version = 1
)
//@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
}