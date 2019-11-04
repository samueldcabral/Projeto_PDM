package com.example.projeto.model

import java.io.Serializable

class Mes : Serializable {
    var id : Int
    var nome : String
    var ano : String
    var pagamentos : ArrayList<Pagamento> = ArrayList<Pagamento>()

    constructor(nome : String, ano : String){
        this.id = -1
        this.nome = nome
        this.ano = ano
    }

    constructor(id : Int, nome : String, ano : String) {
        this.id = id
        this.nome = nome
        this.ano = ano
    }

    fun addPagamento(pagamento : Pagamento) {
        this.pagamentos.add(pagamento)
    }

    fun removePagamento(pagamento : Pagamento) {
        this.pagamentos.remove(pagamento)
    }

    fun removePagamento(index : Int) {
        this.pagamentos.removeAt(index)
    }

    override fun toString(): String {
        return "${nome} - ${ano}"
    }
}