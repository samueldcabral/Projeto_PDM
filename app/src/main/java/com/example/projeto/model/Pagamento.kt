package com.example.projeto.model

import java.io.Serializable
import java.util.*

class Pagamento : Serializable {
    var id : Int
    var nome : String
    var tipo : String
    var status : Boolean = false
    var valor : Double
    var diaDePagamento : String = ""
    lateinit var dataDePagamento : Date
    var obs : String = ""
    var categorias : ArrayList<String> = ArrayList<String>()

    constructor(nome : String, tipo : String, valor : Double) {
        this.id = -1
        this.nome = nome
        this.tipo = tipo
        this.valor = valor
    }

    constructor(id : Int, nome : String, tipo : String, valor : Double) {
        this.id = id
        this.nome = nome
        this.tipo = tipo
        this.valor = valor
    }

    fun addCategoria(categoria : String) {
        this.categorias.add(categoria)
    }

    override fun toString(): String {
        return "${nome} - R$ ${valor} - ${tipo}"
    }
}