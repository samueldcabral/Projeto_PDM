package com.example.projeto

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_floating.*

class FloatingActivity : AppCompatActivity() {
    private lateinit var listaContas : ListView
    private var contas : ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floating)

        fab.setOnClickListener { view ->
            this.goToCadastrarConta()
        }

        this.cadastraContas()
        this.listaContas = findViewById(R.id.lvMainContas)
        this.listaContas.adapter = ContaAdapter(this, this.contas)
        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())

    }

    inner class OnClickLista : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val dialog = AlertDialog.Builder(this@FloatingActivity)
            dialog.setTitle("Atenção")
            dialog.setIcon(R.mipmap.ic_launcher)
            dialog.setMessage("Mensagem")
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

    fun cadastraContas() {
        this.contas.add("Aluguel")
        this.contas.add("Agua")
        this.contas.add("Energia")
        this.contas.add("Internet")
    }

    fun goToCadastrarConta() {
        val it = Intent(this, CadastrarActivity::class.java)
        startActivity(it)
    }

}
