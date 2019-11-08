package com.example.projeto

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.repository.Repository
import kotlinx.coroutines.*
import java.text.DecimalFormat
import kotlin.coroutines.CoroutineContext


class ContaAdapter(var context : Context, var listaContas : List<Pagamento>) : BaseAdapter(),
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var job : Job
    private lateinit var repository : Repository

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        job = Job()

        var layout : View

        if(convertView == null) {
            var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.conta_layout, null)
        }else {
            layout = convertView
        }

        val tv = layout.findViewById<TextView>(R.id.tvConta)
        val tvConta = layout.findViewById<TextView>(R.id.tvContaValor)
        val swConta = layout.findViewById<Switch>(R.id.swConta)
        val pagamento = this.listaContas.get(position)
        tv.text = pagamento.nome
        tvConta.text = "R$ ${pagamento.valor.format(2)}"
        swConta.setOnCheckedChangeListener(OnChecked(pagamento))
        swConta.isChecked = pagamento.status

        return layout
    }

    inner class OnChecked(pagamento : Pagamento) : CompoundButton.OnCheckedChangeListener {
        var pagamento : Pagamento = pagamento

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if(isChecked) {
//                context?.toast("checked the ${pagamento.id} - ${pagamento.nome} - ${pagamento.status}")

                launch {
                    context?.let {
                        val operation = async {
                            repository = Repository(
                            AppDatabase.getDatabase(context!!).UserDao(),
                            AppDatabase.getDatabase(context!!).MesDao(), AppDatabase.getDatabase(context!!).PagamentoDao())

                             var pag2 : Pagamento =  Pagamento(pagamento.nome, pagamento.tipo, true,
                                    pagamento.valor, pagamento.diaDePagamento, pagamento.dataPagamento, pagamento.obs,
                                    pagamento.mes)
                            pag2.id  = pagamento.id

                        repository.updatePagamentos(pag2)
                        }
                        operation.await()
//                        context?.toast("Updated?")
                    }
                }

            }else{
//                context?.toast("unchecked")

            }
        }

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

    fun update() {
        notifyDataSetChanged()
    }


}

private fun Double.format(digits: Int): String {
    return "%.${digits}f".format(this)
}
