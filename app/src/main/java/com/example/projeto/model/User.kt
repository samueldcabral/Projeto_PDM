package com.example.projeto.model

import java.io.Serializable

class User : Serializable {
    var id : Int
    var nome : String
    var meses : ArrayList<Mes> = ArrayList<Mes>()

    constructor(nome : String){
        this.id = -1
        this.nome = nome
    }

    constructor(id : Int, nome : String) {
        this.id = id
        this.nome = nome
    }

    fun getMes(index : Int) : Mes{
        return this.meses.get(index)
    }

    fun getMes(nome : String) : Mes? {
        for(mes in meses) {
            if(mes.nome.equals(nome)){
                return mes
            }
        }
        return null
    }

    fun addMes(mes : Mes) {
        this.meses.add(mes)
    }

    fun removeMes(mes : Mes) {
        this.meses.remove(mes)
    }

    fun removeMes(index : Int) {
        this.meses.removeAt(index)
    }

    override fun toString(): String {
        return "${nome}"
    }
}