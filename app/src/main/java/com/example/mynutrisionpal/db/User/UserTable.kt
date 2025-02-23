package com.example.mynutrisionpal.db.User

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserTable(
    @PrimaryKey
    val email: String,
    val password: String,
    val fullName: String
)