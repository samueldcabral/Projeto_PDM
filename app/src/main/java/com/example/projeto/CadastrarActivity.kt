package com.example.projeto

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.projeto.db.entities.Pagamento

class CadastrarActivity : AppCompatActivity() {
    private lateinit var btSalvar : Button
    private lateinit var btCancelar : Button
    private lateinit var etNomeConta : EditText
    private lateinit var etValorConta : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        this.btSalvar = findViewById(R.id.btCadastrarSalvar)
        this.btCancelar = findViewById(R.id.btCadastrarCancelar)
        this.etNomeConta = findViewById(R.id.etCadastrarNomeConta)
        this.etValorConta = findViewById(R.id.etCadastrarValorConta)

        this.btSalvar.setOnClickListener(ClickSalvar(it = btSalvar))
        this.btCancelar.setOnClickListener({ finish() })
    }

    inner class ClickSalvar(it: View) : View.OnClickListener {
        override fun onClick(v: View?) {
            val nomePagamento : String = this@CadastrarActivity.etNomeConta.text.toString()
            var valorPagamento : String = this@CadastrarActivity.etValorConta.text.toString()
            valorPagamento = valorPagamento.replace(',', '.')
            val tipoPagamento = "Mensal"

            val pagamento : Pagamento = Pagamento(nomePagamento, tipoPagamento, false, valorPagamento.toDouble(), null, null, null, null)

            val itCadastro = Intent()
            itCadastro.putExtra("Pagamento", pagamento)

            setResult(Activity.RESULT_OK, itCadastro)
            finish()
        }
    }
}
