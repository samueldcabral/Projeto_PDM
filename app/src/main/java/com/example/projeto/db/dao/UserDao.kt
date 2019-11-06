package com.example.projeto.db.dao

import androidx.room.*
import com.example.projeto.db.entities.User

@Dao
interface UserDao {

    //Select
    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers() : List<User>

    @Query("SELECT * FROM user_table WHERE nome LIKE :nome LIMIT 1")
    suspend fun findUserByName(nome : String) : User

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun findUserById(id : Int) : User

    //Insert
    @Insert
    suspend fun insertUser(user : User) : Long

    //Update
    @Update
    suspend fun updateUser(user : User) : Int

    //Delete
    @Delete
    suspend fun deleteUser(user : User) : Int
}