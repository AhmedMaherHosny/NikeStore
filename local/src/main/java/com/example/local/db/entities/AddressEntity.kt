package com.example.local.db.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "address_line_1")
    val addressLine1: String? = null,
    @ColumnInfo(name = "address_line_2")
    val addressLine2: String? = null,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,
    @ColumnInfo(name = "zip_code")
    val zipCode: String? = null,
    @ColumnInfo(name = "country")
    val country: String? = null,
    @ColumnInfo(name = "city")
    val city: String? = null,
)