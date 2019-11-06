package com.example.projeto.db.dao

import androidx.room.*
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.User

@Dao
interface MesDao {
    //Select
    @Query("SELECT * FROM mes_table")
    suspend fun getAllMeses() : List<Mes>

    @Query("SELECT * FROM mes_table WHERE user_id = :userId ")
    suspend fun findMesesByUser(userId : Int) : List<Mes>

    @Query("SELECT * FROM mes_table WHERE nome LIKE :nome LIMIT 1")
    suspend fun findMesByName(nome : String) : Mes

    @Query("SELECT * FROM mes_table WHERE id = :id")
    suspend fun findMesById(id : Int) : Mes

    //Insert
    @Insert
    suspend fun insertMes(mes : Mes) : Long

    //Update
    @Update
    suspend fun updateMes(mes : Mes) : Int

    //Delete
    @Delete
    suspend fun deleteMes(mes : Mes) : Int
}