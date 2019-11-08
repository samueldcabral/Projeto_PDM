package com.example.projeto.db.dao

import androidx.room.*
import com.example.projeto.db.entities.Pagamento

@Dao
interface PagamentoDao {

    //Select
    @Query("SELECT * FROM pagamento_table")
    suspend fun getAllPagamentos() : List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE tipo = 'Mensal'")
    suspend fun getAllPagamentosMensais(): List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE mes_id = :mesId ")
    suspend fun findPagamentosByMes(mesId : Int) : List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE mes_id = :mesDefault AND tipo = :tipo")
    suspend fun findPagamentosDiariosByMes(mesDefault: Int, tipo : String): List<Pagamento>

    @Query("SELECT * FROM pagamento_table WHERE mes_id = :id AND tipo = :tipo")
    suspend fun findPagamentosMensaisByMes(id: Int, tipo: String): List<Pagamento>

    @Query("SELECT SUM(valor) FROM pagamento_table WHERE mes_id = :mesDefault AND tipo = :tipo ")
    suspend fun findSomaPagamentosDiariosByMes(mesDefault: Int, tipo: String): Double

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