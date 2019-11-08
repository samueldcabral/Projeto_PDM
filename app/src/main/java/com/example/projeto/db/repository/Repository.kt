package com.example.projeto.db.repository

import android.util.Log
import com.example.projeto.db.dao.MesDao
import com.example.projeto.db.dao.PagamentoDao
import com.example.projeto.db.dao.UserDao
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.entities.User

class Repository (private val userDao: UserDao, private val mesDao : MesDao, private val pagamentoDao: PagamentoDao) {

    /////////////////////////////////////////////////////////////////////////////////
    //Select
    suspend fun getAllUsers() : List<User> {
        return userDao.getAllUsers()
    }

    suspend fun findUserByName(nome : String) : User {
        return userDao.findUserByName(nome)
    }

    suspend fun getAllMeses() : List<Mes> {
        return mesDao.getAllMeses()
    }

    suspend fun getLastMes() : Mes {
        return mesDao.getLastMes()
    }

    suspend fun getSecondToLastMes() : Mes {
        var meses = mesDao.getSecondToLastMes()
        return meses.get(1)
    }

    suspend fun findMesesByUser(userId : Int) : List<Mes> {
        return mesDao.findMesesByUser(userId)
    }

    suspend fun findMesById(id : Int) : Mes {
        return mesDao.findMesById(id)
    }

    suspend fun getAllPagamentos() : List<Pagamento> {
        return pagamentoDao.getAllPagamentos()
    }

    suspend fun findPagamentosMensaisByMes(id: Int): List<Pagamento>  {
        var tipo = "Mensal"
        return pagamentoDao.findPagamentosMensaisByMes(id, tipo)
    }

    suspend fun findPagamentosDiariosByMes(mesDefault: Int, tipo : String): List<Pagamento> {
        return pagamentoDao.findPagamentosDiariosByMes(mesDefault, tipo)
    }

    suspend fun findSomaPagamentosDiariosByMes(mesDefault: Int, tipoPagamento: String): Double {
        return pagamentoDao.findSomaPagamentosDiariosByMes(mesDefault, tipoPagamento)
    }

    suspend fun findPagamentoById(id : Int) : Pagamento {
        return pagamentoDao.findPagamentoById(id)
    }

    /////////////////////////////////////////////////////////////////////////////////
    //Insert
    suspend fun insertUser(user : User) : Long {
        return userDao.insertUser(user)
    }

    suspend fun insertMes(mes : Mes) : Long {
        var nome  = getMesNome(mes.num_mes)
        mes.nome = nome
        return mesDao.insertMes(mes)
    }

    suspend fun insertNovoMes() : Mes {
        var mes : Mes = getLastMes()
        var novoMes = getNextMes(mes)
        novoMes.id = insertMes(novoMes).toInt()
        return novoMes
    }

    suspend fun insertMesOnPagamentosMensais(id: Int) {
        var mes = getSecondToLastMes()
        var pagamentos = findPagamentosMensaisByMes(mes.id)

        for(pag in pagamentos) {
            var novoPag = Pagamento(pag.nome, pag.tipo, false, pag.valor, null, null, null, id)
            insertPagamentos(novoPag)
        }
    }

    suspend fun insertPagamentos(pagamento : Pagamento) : Long {
        return pagamentoDao.insertPagamentos(pagamento)
    }

    suspend fun insertUserOnMes(mes : Int, user : Int) {
        var mes = findMesById(mes)
        mes.user = user

        updateMes(mes)
    }

    suspend fun insertMesOnPagamento(pagamento : Int, mes : Int) {
        var pagamento = findPagamentoById(pagamento)
        pagamento.mes = mes
        updatePagamentos(pagamento)
    }


    /////////////////////////////////////////////////////////////////////////////////
    //Update
    suspend fun updateUser(user : User) : Int{
        return userDao.updateUser(user)
    }

    suspend fun updateMes(mes : Mes) : Int {
        return mesDao.updateMes(mes)
    }

    suspend fun updatePagamentos(pagamento : Pagamento) {
        return pagamentoDao.updatePagamentos(pagamento)
    }


    /////////////////////////////////////////////////////////////////////////////////
    //Delete
    suspend fun deleteUser(user : User) : Int {
        return userDao.deleteUser(user)
    }

    suspend fun deleteMes(mes : Mes) : Int {
        return mesDao.deleteMes(mes)
    }

    suspend fun deletePagamentos(pagamento : Pagamento) {
        return pagamentoDao.deletePagamentos(pagamento)
    }

    //Helper

    private fun getNextMes(mes : Mes) : Mes {
        var mes_num = mes.num_mes
        var mes_ano = mes.ano

        mes_num++

        if(mes_num > 12) {
            mes_num = 1
            mes_ano++
        }

        mes.id++
        mes.nome = getMesNome(mes_num)
        mes.num_mes = mes_num
        mes.ano = mes_ano

        return mes
    }

    private fun getMesNome(num : Int) : String {
        val nome = when(num){
            1 -> "Janeiro"
            2 -> "Fevereiro"
            3 -> "Março"
            4 -> "Abril"
            5 -> "Maio"
            6 -> "Junho"
            7 -> "Julho"
            8 -> "Agosto"
            9 -> "Setembro"
            10 -> "Outubro"
            11 -> "Novembro"
            12 -> "Dezembro"
            else -> "Invalido"
        }
        return nome
    }

}
