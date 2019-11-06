package com.example.projeto.db.repository

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

    suspend fun findUserById(id : Int) : User {
        return userDao.findUserById(id)
    }

    suspend fun getAllMeses() : List<Mes> {
        return mesDao.getAllMeses()
    }

    suspend fun findMesesByUser(userId : Int) : List<Mes> {
        return mesDao.findMesesByUser(userId)
    }

    suspend fun findMesByName(nome : String) : Mes {
        return mesDao.findMesByName(nome)
    }

    suspend fun findMesById(id : Int) : Mes {
        return mesDao.findMesById(id)
    }

    suspend fun getAllPagamentos() : List<Pagamento> {
        return pagamentoDao.getAllPagamentos()
    }

    suspend fun findPagamentosByMes(mesId : Int) : List<Pagamento> {
        return pagamentoDao.findPagamentosByMes(mesId)
    }

    suspend fun findPagamentoById(id : Int) : Pagamento {
        return pagamentoDao.findPagamentoById(id)
    }

    suspend fun findPagamentoByName(nome : String) : Pagamento {
        return pagamentoDao.findPagamentoByName(nome)
    }

    /////////////////////////////////////////////////////////////////////////////////
    //Insert
    suspend fun insertUser(user : User) : Long {
        return userDao.insertUser(user)
    }

    suspend fun insertMes(mes : Mes) : Long {
        return mesDao.insertMes(mes)
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

}