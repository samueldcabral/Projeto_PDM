package com.example.projeto.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.projeto.*
import com.example.projeto.adapters.DiarioAdapter
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.repository.Repository
import com.example.projeto.helpers.log
import com.example.projeto.helpers.toast

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class DiarioFragment : BaseFragment() {
    private val TIPO_PAGAMENTO = "Diário"
    private lateinit var listaContas: ListView
    private lateinit var tvResumo : TextView
    private lateinit var tvMes: TextView
    private lateinit var etNome: EditText
    private lateinit var etValor: EditText
    private lateinit var btSalvar: Button
    private lateinit var repository : Repository
    private lateinit var pagamentos : ArrayList<Pagamento>
    private var totalDiarios : Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_diario, container, false)

        this.tvResumo = layout.findViewById(R.id.tvDiarioResumo)
        this.listaContas = layout.findViewById(R.id.lvDiarioContas)
        this.tvMes = layout.findViewById(R.id.tvDiarioMes)
        this.etNome = layout.findViewById(R.id.etDiarioNome)
        this.etValor = layout.findViewById(R.id.etDiarioValor)
        this.btSalvar = layout.findViewById(R.id.btDiarioSalvar)

        this.btSalvar.setOnClickListener(CadastrarDiario())

        this.tvMes.text = TIPO_PAGAMENTO

        launch {
            repository = Repository(
                AppDatabase.getDatabase(context!!).UserDao(),
                AppDatabase.getDatabase(context!!).MesDao(), AppDatabase.getDatabase(context!!).PagamentoDao())

            var ultimoMes = repository.getLastMes()

            if(ultimoMes != null) {
                pagamentos = repository.findPagamentosDiariosByMes(ultimoMes.id, TIPO_PAGAMENTO) as ArrayList<Pagamento>

                if(pagamentos.size > 0) {
                    var total = repository.findSomaPagamentosDiariosByMes(ultimoMes.id, TIPO_PAGAMENTO)
                    tvResumo.text = "Seus gastos diários totais: R$ ${total.format(2)}"
                }

                listaContas.adapter = getContext()?.let {
                    DiarioAdapter(
                        it,
                        pagamentos
                    )
                }
            }

        }

        this.listaContas.setOnItemLongClickListener(OnLongClickLista())

        return layout
    }

    inner class OnLongClickLista : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ): Boolean {
            var pag = pagamentos.get(pagamentos.size - 1 - position)
            context?.toast("clicou long no ${pag.nome} ${pag.id} ${pag.valor}")

            launch {
                var operation = async {
                    repository.deletePagamentos(pag)
                    var mesAtual = repository.getLastMes()
                    totalDiarios = repository.findSomaPagamentosDiariosByMes(mesAtual.id, TIPO_PAGAMENTO)

                    tvResumo.text = "Seus gastos diários totais: R$ ${totalDiarios.format(2)}"

                    this@DiarioFragment.pagamentos.clear()
                    this@DiarioFragment.pagamentos.addAll(repository.findPagamentosDiariosByMes(mesAtual.id, TIPO_PAGAMENTO) as ArrayList<Pagamento>)

                    (this@DiarioFragment.listaContas.adapter as DiarioAdapter).update()
                }
                operation.await()
            }

            return true
        }

    }

    inner class CadastrarDiario : View.OnClickListener {
        override fun onClick(v: View?) {
            var nome = etNome.text.toString()
            var valor = etValor.text.toString().replace(',', '.')

            if(nome == "") {
                context?.toast("Digite um nome")
                return
            }
            if(valor == "") {
                context?.toast("Digite um valor")
                return
            }

            var newValor : Double = valor.toDouble()

            launch {
                var operation = async {
                    var mesAtual = repository.getLastMes()
                    var pagamento = Pagamento(nome, TIPO_PAGAMENTO, false, newValor, null,null,null, mesAtual.id)
                    repository.insertPagamentos(pagamento)
                    totalDiarios = repository.findSomaPagamentosDiariosByMes(mesAtual.id, TIPO_PAGAMENTO)
                    tvResumo.text = "Seus gastos diários totais: R$ ${totalDiarios.format(2)}"

                    this@DiarioFragment.pagamentos.clear()
                    this@DiarioFragment.pagamentos.addAll(repository.findPagamentosDiariosByMes(mesAtual.id, TIPO_PAGAMENTO) as ArrayList<Pagamento>)

                    (this@DiarioFragment.listaContas.adapter as DiarioAdapter).update()
                }
                operation.await()

            }

            etNome.setText("")
            etValor.setText("")
        }
    }

    private fun Double.format(digits: Int): String {
        return "%.${digits}f".format(this)
    }
}

