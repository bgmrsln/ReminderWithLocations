package com.codemave.mobilecomputing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "logininfos",
    indices = [
        Index("user_id", unique = true),
        Index("username", unique = true)
    ]
)
data class LoginInfo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val userId: Long=0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name= "cuurent_location_x") val currentLocationX: Double?,
    @ColumnInfo(name= "current_location_y") val currentLocationY: Double?

)