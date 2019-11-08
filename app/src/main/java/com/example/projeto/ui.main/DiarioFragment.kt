package com.example.projeto.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.ActionBarContextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projeto.*
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.repository.Repository
//import com.example.projeto.FloatingActivity

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.dialog_diario.view.*
import kotlinx.android.synthetic.main.dialog_mes.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class DiarioFragment : BaseFragment() {
    private var FORM_CADASTRAR = 1
    private val TIPO_PAGAMENTO = "Diário"
//    private val MES_DEFAULT = 8
    private lateinit var listaContas: ListView
    //    private lateinit var mes : Mes
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
//        return inflater.inflate(R.layout.activity_floating, container, false)
        var layout = inflater.inflate(R.layout.fragment_diario, container, false)
//        val fab: FloatingActionButton = layout.findViewById(R.id.fab)
//
//        fab.setOnClickListener { view ->
////            this.goToCadastrarConta()
//        }

//        this.cadastraContas()
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

                context?.log("pagamentos [e ${pagamentos.size} - ${pagamentos}")

                if(pagamentos.size > 0) {
                    var total = repository.findSomaPagamentosDiariosByMes(ultimoMes.id, TIPO_PAGAMENTO)
                    tvResumo.text = "Seus gastos diários totais: R$ ${totalDiarios.format(2)}"

                }

                listaContas.adapter = getContext()?.let { DiarioAdapter(it, pagamentos) }

            }



        }
//        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnLongClick())
//

        return layout
    }

    inner class OnLongClick : AdapterView.OnItemLongClickListener {
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
//
        }

    }


//    fun cadastraContas() {
////        this.mes = Mes("Dezembro", "2019")
////        this.mes.addPagamento(Pagamento("Lanche 1", TIPO_PAGAMENTO, 5.00))
////        this.mes.addPagamento(Pagamento("Paozinho", TIPO_PAGAMENTO, 5.00))
////        this.mes.addPagamento(Pagamento("Pedágio", TIPO_PAGAMENTO, 1.00))
////        this.mes.addPagamento(Pagamento("Doaçao", TIPO_PAGAMENTO, 5.00))
////        this.mes.addPagamento(Pagamento("Salgado 1 Real", TIPO_PAGAMENTO, 5.00))
////        this.mes.addPagamento(Pagamento("Feijoada", TIPO_PAGAMENTO, 15.00))
////        this.mes.addPagamento(Pagamento("Coquinha", TIPO_PAGAMENTO, 2.00))
////        this.mes.addPagamento(Pagamento("Pedágio", TIPO_PAGAMENTO, 5.00))
//    }

//    fun goToCadastrarConta() {
//        val it = Intent(context, CadastrarActivity::class.java)
//        startActivityForResult(it, FORM_CADASTRAR)
//    }

    fun atualizaLista() {
        (this.listaContas.adapter as DiarioAdapter).update()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == FORM_CADASTRAR) {
////                val pagamento = data?.getSerializableExtra("PAGAMENTO") as Pagamento
////                this.mes.addPagamento(pagamento)
//                this.atualizaLista()
//            }
//        }
//    }

    private fun Double.format(digits: Int): String {
        return "%.${digits}f".format(this)
    }

    inner class OnClickLista : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_diario, null)
////            var pagamento = this@DiarioFragment.mes.pagamentos.get(this@DiarioFragment.mes.pagamentos.size - 1 - position)
//            mDialogView.etDialogNomeDiario.text.append(pagamento.nome)
//            mDialogView.etDialogValorDiario.text.append(pagamento.valor.toString())
//
//            val mDialogBuilder = AlertDialog.Builder(context)
//            mDialogBuilder.setTitle(pagamento.nome)
//            mDialogBuilder.setView(mDialogView)
//
//            var mAlertDialog = mDialogBuilder.show()
//
//            mDialogView.btDialogSalvarDiario.setOnClickListener {
//                mAlertDialog.dismiss()
//                pagamento.nome = mDialogView.etDialogNomeDiario.text.toString()
//                var valor: String = mDialogView.etDialogValorDiario.text.toString()
//                valor = valor.replace(',', '.')
//                pagamento.valor = valor.toDouble()
//                this@DiarioFragment.atualizaLista()
//            }
//
//            mDialogView.btDialogCancelarDiario.setOnClickListener {
//                mAlertDialog.dismiss()
//            }
//
//            this@DiarioFragment.atualizaLista()
//        }
        }

        inner class OnItemLongClickLista : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ): Boolean {
//            this@DiarioFragment.mes.removePagamento(this@DiarioFragment.mes.pagamentos.size - 1 - position)

//                launch {
//                    var operation = async {
//                        var result = repository.findPagamentoById(position - 1)
//
//                        tvResumo.text = "Seus gastos diários totais: R$ ${totalDiarios.format(2)}"
//
//                        this@DiarioFragment.pagamentos.clear()
//                        this@DiarioFragment.pagamentos.addAll(repository.findPagamentosDiariosByMes(MES_DEFAULT, TIPO_PAGAMENTO) as ArrayList<Pagamento>)
//
//                        (this@DiarioFragment.listaContas.adapter as DiarioAdapter).update()
//                    }
//                    operation.await()
//
//                }

                return true
            }

        }
    }
}

