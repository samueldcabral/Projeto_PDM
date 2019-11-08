package com.example.projeto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento


class DiarioAdapter(var context : Context, var listaContas : List<Pagamento>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var layout : View

        if(convertView == null) {
            var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.diario_layout, null)
        }else {
            layout = convertView
        }

        val tv = layout.findViewById<TextView>(R.id.tvDiarioConta)
        val tvConta = layout.findViewById<TextView>(R.id.tvDiarioValor)

        var pagamento = this.listaContas.get(this.listaContas.size - 1 - position)
        tv.text = pagamento.nome
        tvConta.text = "R$ ${pagamento.valor.format(2)}"

        return layout
    }

    override fun getItem(position: Int): Any {
        return this.listaContas.get(position)
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getCount(): Int {
        return this.listaContas.count()
    }

    private fun Double.format(digits: Int): String {
        return "%.${digits}f".format(this)
    }

    fun update() {
        notifyDataSetChanged()
    }
}