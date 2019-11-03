package com.example.projeto

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto.model.Mes
import com.example.projeto.model.Pagamento

import kotlinx.android.synthetic.main.activity_floating.*

class FloatingActivity : AppCompatActivity() {
    private var FORM_CADASTRAR = 1
    private lateinit var listaContas : ListView
    private lateinit var mes : Mes
    private lateinit var tvMes : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floating)

        fab.setOnClickListener { view ->
            this.goToCadastrarConta()
        }

        this.cadastraContas()

        this.listaContas = findViewById(R.id.lvMainContas)
        this.tvMes = findViewById(R.id.tvMainMes)

        this.tvMes.text = "${mes.nome} ${mes.ano}"
        this.listaContas.adapter = ContaAdapter(this, this.mes, this.mes.pagamentos)
        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())

    }

    fun cadastraContas() {
        this.mes = Mes("Dezembro", "2019")
        this.mes.addPagamento(Pagamento("Aluguel", "Mensal", 500.00))
        this.mes.addPagamento(Pagamento("Agua", "Mensal", 50.00))
        this.mes.addPagamento(Pagamento("Energia", "Mensal", 100.00))
        this.mes.addPagamento(Pagamento("Internet", "Mensal", 50.00))
    }

    fun goToCadastrarConta() {
        val it = Intent(this, CadastrarActivity::class.java)
        startActivityForResult(it, FORM_CADASTRAR)
    }

    fun atualizaLista() {
        (this.listaContas.adapter as ContaAdapter).notifyDataSetChanged()
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

    inner class OnClickLista : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val dialog = AlertDialog.Builder(this@FloatingActivity)
            dialog.setTitle(this@FloatingActivity.mes.pagamentos.get(position).nome)
            dialog.setIcon(R.mipmap.ic_launcher)
            dialog.setMessage("Alguma coisa relevante sobre a conta")
            dialog.setView(EditText(this@FloatingActivity))
            dialog.setPositiveButton("Ok", ClickButton())
            dialog.setNegativeButton("Cancela"
                , null)
            dialog.create().show()
        }

        private fun ClickButton(): DialogInterface.OnClickListener? {
            return null
        }
    }

    inner class OnItemLongClickLista : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ): Boolean {
            Toast.makeText(this@FloatingActivity, "WHAT", Toast.LENGTH_SHORT)
            return true
        }

    }
}
