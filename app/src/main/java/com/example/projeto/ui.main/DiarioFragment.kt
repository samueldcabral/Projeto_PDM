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
import androidx.fragment.app.Fragment
import com.example.projeto.CadastrarActivity
import com.example.projeto.ContaAdapter
import com.example.projeto.DiarioAdapter
//import com.example.projeto.FloatingActivity
import com.example.projeto.R
import com.example.projeto.model.Mes
import com.example.projeto.model.Pagamento
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_diario.view.*
import kotlinx.android.synthetic.main.dialog_mes.view.*

class DiarioFragment : Fragment() {
    private var FORM_CADASTRAR = 1
    private val TIPO_PAGAMENTO = "Diário"
    private lateinit var listaContas : ListView
    private lateinit var mes : Mes
    private lateinit var tvMes : TextView
    private lateinit var etNome : EditText
    private lateinit var etValor : EditText
    private lateinit var btSalvar : Button

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

        this.cadastraContas()

        this.listaContas = layout.findViewById(R.id.lvDiarioContas)
        this.tvMes = layout.findViewById(R.id.tvDiarioMes)
        this.etNome = layout.findViewById(R.id.etDiarioNome)
        this.etValor = layout.findViewById(R.id.etDiarioValor)
        this.btSalvar = layout.findViewById(R.id.btDiarioSalvar)

        this.btSalvar.setOnClickListener(CadastrarDiario())

        this.tvMes.text = TIPO_PAGAMENTO
        this.listaContas.adapter = getContext()?.let { DiarioAdapter(it, this.mes, this.mes.pagamentos) }
        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())


        return layout
    }

    inner class CadastrarDiario : View.OnClickListener {
        override fun onClick(v: View?) {
            var nome = etNome.text.toString()
            var valor = etValor.text.toString().replace(',', '.').toDouble()
            var pagamento = Pagamento(nome, TIPO_PAGAMENTO, valor)

            etNome.setText("")
            etValor.setText("")
            this@DiarioFragment.mes.addPagamento(pagamento)
            this@DiarioFragment.atualizaLista()
        }

    }


    fun cadastraContas() {
        this.mes = Mes("Dezembro", "2019")
        this.mes.addPagamento(Pagamento("Lanche 1", TIPO_PAGAMENTO, 5.00))
        this.mes.addPagamento(Pagamento("Paozinho", TIPO_PAGAMENTO, 5.00))
        this.mes.addPagamento(Pagamento("Pedágio", TIPO_PAGAMENTO, 1.00))
        this.mes.addPagamento(Pagamento("Doaçao", TIPO_PAGAMENTO, 5.00))
        this.mes.addPagamento(Pagamento("Salgado 1 Real", TIPO_PAGAMENTO, 5.00))
        this.mes.addPagamento(Pagamento("Feijoada", TIPO_PAGAMENTO, 15.00))
        this.mes.addPagamento(Pagamento("Coquinha", TIPO_PAGAMENTO, 2.00))
        this.mes.addPagamento(Pagamento("Pedágio", TIPO_PAGAMENTO, 5.00))
    }

//    fun goToCadastrarConta() {
//        val it = Intent(context, CadastrarActivity::class.java)
//        startActivityForResult(it, FORM_CADASTRAR)
//    }

    fun atualizaLista() {
        (this.listaContas.adapter as DiarioAdapter).notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == FORM_CADASTRAR){
                val pagamento = data?.getSerializableExtra("PAGAMENTO") as Pagamento
                this.mes.addPagamento(pagamento)
                this.atualizaLista()
            }
        }
    }

    inner class OnClickLista : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_diario, null)
            var pagamento = this@DiarioFragment.mes.pagamentos.get(this@DiarioFragment.mes.pagamentos.size - 1 - position)
            mDialogView.etDialogNomeDiario.text.append(pagamento.nome)
            mDialogView.etDialogValorDiario.text.append(pagamento.valor.toString())

            val mDialogBuilder = AlertDialog.Builder(context)
            mDialogBuilder.setTitle(pagamento.nome)
            mDialogBuilder.setView(mDialogView)

            var mAlertDialog = mDialogBuilder.show()

            mDialogView.btDialogSalvarDiario.setOnClickListener {
                mAlertDialog.dismiss()
                pagamento.nome = mDialogView.etDialogNomeDiario.text.toString()
                var valor: String = mDialogView.etDialogValorDiario.text.toString()
                valor = valor.replace(',', '.')
                pagamento.valor = valor.toDouble()
                this@DiarioFragment.atualizaLista()
            }

            mDialogView.btDialogCancelarDiario.setOnClickListener {
                mAlertDialog.dismiss()
            }

            this@DiarioFragment.atualizaLista()
        }
    }

    inner class OnItemLongClickLista : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ): Boolean {
            this@DiarioFragment.mes.removePagamento(this@DiarioFragment.mes.pagamentos.size - 1 - position)
            this@DiarioFragment.atualizaLista()
            return true
        }

    }
}

