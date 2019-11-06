package com.example.projeto.db.dao

import androidx.room.*
import com.example.projeto.db.entities.Pagamento

@Dao
interface PagamentoDao {

    //Select
    @Query("SELECT * FROM pagamento_table")
    suspend fun getAllPagamentos() : List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE mes_id = :mesId ")
    suspend fun findPagamentosByMes(mesId : Int) : List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE nome LIKE :nome LIMIT 1")
    suspend fun findPagamentoByName(nome : String) : Pagamento

    @Query("SELECT * FROM pagamento_table WHERE id = :id")
    suspend fun findPagamentoById(id : Int) : Pagamento

    //Insert
    @Insert
    suspend fun insertPagamentos(pagamento : Pagamento) : Long

    //Update
    @Update
    suspend fun updatePagamentos(pagamento : Pagamento)

    //Delete
    @Delete
    suspend fun deletePagamentos(pagamento : Pagamento)
}