package com.example.projeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.projeto.model.Pagamento

class CadastrarActivity : AppCompatActivity() {
    private lateinit var btSalvar : Button
    private lateinit var btCancelar : Button
    private lateinit var etNomeConta : EditText
    private lateinit var etValorConta : EditText
//    private lateinit var tvValor : TextView
//    private lateinit var sbValor : SeekBar
//    private lateinit var npValor : NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        this.btSalvar = findViewById(R.id.btCadastrarSalvar)
        this.btCancelar = findViewById(R.id.btCadastrarCancelar)
        this.etNomeConta = findViewById(R.id.etCadastrarNomeConta)
        this.etValorConta = findViewById(R.id.etCadastrarValorConta)

//        this.tvValor = findViewById(R.id.tvCadastrarValorConta)
//        this.sbValor = findViewById(R.id.sbCadastrarValorConta)
//        this.npValor = findViewById(R.id.npCadastrarValorConta)
//        this.npValor.minValue = 0
//        this.npValor.maxValue = 100

        this.btSalvar.setOnClickListener(ClickSalvar(it = btSalvar))
        this.btCancelar.setOnClickListener({ finish() })
//        this.sbValor.setOnSeekBarChangeListener(ChangeSeekBar())
//        this.tvValor.text = this.sbValor.progress.toString()
    }

    fun mudarIntercionalizacao(valor : String) : String {
        return valor.replace(',', '.')
    }

    inner class ClickSalvar(it: View) : View.OnClickListener {
        override fun onClick(v: View?) {
            val nomePagamento : String = this@CadastrarActivity.etNomeConta.text.toString()
            var valorPagamento : String = this@CadastrarActivity.etValorConta.text.toString()
            valorPagamento = this@CadastrarActivity.mudarIntercionalizacao(valorPagamento)
            val tipoPagamento = "Mensal"

            val pagamento = Pagamento(nomePagamento, tipoPagamento, valorPagamento.toDouble())

            val itCadastro = Intent()
            itCadastro.putExtra("PAGAMENTO", pagamento)

            setResult(Activity.RESULT_OK, itCadastro)
            finish()
        }

    }

//    inner class ChangeSeekBar : SeekBar.OnSeekBarChangeListener {
//        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//            this@CadastrarActivity.tvValor.text = progress.toString()
//        }
//
//        override fun onStartTrackingTouch(seekBar: SeekBar?) {
//        }
//
//        override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        }
//
//    }

}
