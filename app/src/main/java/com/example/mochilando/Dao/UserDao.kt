package com.example.mochilando.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.mochilando.Entity.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Upsert
    suspend fun upsert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun findById(id: Int) : User?

    @Query("SELECT * FROM User ORDER BY name")
    suspend fun findAll() : List<User>

    @Query("SELECT * FROM User WHERE name = :user AND password = :password")
    suspend fun login(user: String, password: String) : User?
}