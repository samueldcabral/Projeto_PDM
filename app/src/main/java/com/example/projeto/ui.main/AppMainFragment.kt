package com.example.projeto.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projeto.CadastrarActivity
import com.example.projeto.ContaAdapter
//import com.example.projeto.FloatingActivity
import com.example.projeto.R
import com.example.projeto.model.Mes
import com.example.projeto.model.Pagamento
import com.example.projeto.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_mes.*
import kotlinx.android.synthetic.main.dialog_mes.view.*

class AppMainFragment : Fragment() {
    private var FORM_CADASTRAR = 1
    private var TIPO_PAGAMENTO = "Mensal"
    private lateinit var mesAtual : Mes
    private lateinit var listaContas : ListView
    private lateinit var user : User
    private lateinit var tvMes : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.activity_floating, container, false)
        var layout = inflater.inflate(R.layout.activity_floating, container, false)
        val fab: FloatingActionButton = layout.findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            this.goToCadastrarConta()
        }

        this.cadastraContas()

        this.listaContas = layout.findViewById(R.id.lvMainContas)
        this.tvMes = layout.findViewById(R.id.tvMainMes)

        this.tvMes.text = "${mesAtual.nome} ${mesAtual.ano}"
        this.tvMes.setOnClickListener(Clicked())

        this.listaContas.adapter = getContext()?.let { ContaAdapter(it, this.mesAtual, this.mesAtual.pagamentos) }
        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())


        return layout
    }

    inner class Clicked : View.OnClickListener {
        override fun onClick(v: View?) {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mes, null)

            val mDialogBuilder = AlertDialog.Builder(context)
            mDialogBuilder.setTitle("wualquer")
            mDialogBuilder.setView(mDialogView)

            mDialogBuilder.create().show()
        }

    }


    fun cadastraContas() {
        this.user = User("Samuel")
        var mes = Mes("Dezembro", "2019")
        user.addMes(mes)
        this.mesAtual = mes
        mes.addPagamento(Pagamento("Aluguel", TIPO_PAGAMENTO, 500.00))
        mes.addPagamento(Pagamento("Agua", TIPO_PAGAMENTO, 50.00))
        mes.addPagamento(Pagamento("Energia", TIPO_PAGAMENTO, 100.00))
        mes.addPagamento(Pagamento("Internet", TIPO_PAGAMENTO, 50.00))
    }

    fun goToCadastrarConta() {
        val it = Intent(context, CadastrarActivity::class.java)
        startActivityForResult(it, FORM_CADASTRAR)
    }

    fun atualizaLista() {
        (this.listaContas.adapter as ContaAdapter).notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == FORM_CADASTRAR){
                val pagamento = data?.getSerializableExtra("PAGAMENTO") as Pagamento
                this.mesAtual.addPagamento(pagamento)
                this.atualizaLista()
            }
        }
    }

    inner class OnClickLista : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mes, null)
            var pagamento = this@AppMainFragment.mesAtual.pagamentos.get(position)
            mDialogView.etDialogNome.text.append(pagamento.nome)
            mDialogView.etDialogValor.text.append(pagamento.valor.toString())
            mDialogView.tvDialogDataPag.text = pagamento.diaDePagamento.toString()
            mDialogView.swDialogStatus.isChecked = pagamento.status
            mDialogView.etDialogObs.text.append(pagamento.obs)

            val mDialogBuilder = AlertDialog.Builder(context)
            mDialogBuilder.setTitle(pagamento.nome)
            mDialogBuilder.setView(mDialogView)

            var mAlertDialog = mDialogBuilder.show()

            mDialogView.btDialogSalvar.setOnClickListener{
                mAlertDialog.dismiss()
                pagamento.nome = mDialogView.etDialogNome.text.toString()
                var valor : String = mDialogView.etDialogValor.text.toString()
                valor = valor.replace(',', '.')
                pagamento.valor = valor.toDouble()
                pagamento.obs = mDialogView.etDialogObs.text.toString()
                pagamento.status = mDialogView.swDialogStatus.isChecked
                this@AppMainFragment.atualizaLista()
            }

            mDialogView.btDialogCancelar.setOnClickListener{
                mAlertDialog.dismiss()
            }

            this@AppMainFragment.atualizaLista()
        }
    }

    inner class OnItemLongClickLista : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ): Boolean {
            this@AppMainFragment.mesAtual.removePagamento(position)
            this@AppMainFragment.atualizaLista()
            return true
        }

    }
}

