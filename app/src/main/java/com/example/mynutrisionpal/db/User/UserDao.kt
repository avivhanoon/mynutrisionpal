package com.example.mynutrisionpal.db.User

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserTable)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserTable?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserTable>

    @Update
    suspend fun updateUser(user: UserTable)

    @Delete
    suspend fun deleteUser(user: UserTable)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun doesUserExist(email: String): Boolean
}