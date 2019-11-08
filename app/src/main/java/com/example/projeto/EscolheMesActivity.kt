package com.example.projeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import com.example.projeto.adapters.EscolhaAdapter
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.repository.Repository
import com.example.projeto.helpers.log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EscolheMesActivity() : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job
    private lateinit var btAdicionar : Button
    private lateinit var lvEscolha : ListView
    private lateinit var repository : Repository
    private lateinit var meses : ArrayList<Mes>

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escolhe_mes)

        //Coroutine
        job = Job()

        this.btAdicionar = findViewById(R.id.btEscolhaAdicionar)
        this.lvEscolha = findViewById(R.id.lvEscolhaLista)

        meses = intent.getSerializableExtra("MESES") as ArrayList<Mes>

        this.btAdicionar.setOnClickListener(OnClickBotaoAdicionar())
        this.lvEscolha.adapter = EscolhaAdapter(this, meses)
        this.lvEscolha.setOnItemClickListener(OnItemCLickLista())

        launch {
            //Instanciando o repositorio
            repository = Repository(
                AppDatabase.getDatabase(this@EscolheMesActivity!!).UserDao(),
                AppDatabase.getDatabase(this@EscolheMesActivity!!).MesDao(),
                AppDatabase.getDatabase(this@EscolheMesActivity!!).PagamentoDao()
            )
        }
    }

    inner class OnClickBotaoAdicionar : View.OnClickListener {
        override fun onClick(v: View?) {

            launch {
                var operation = async {
                    var novoMes = repository.insertNovoMes()
                    repository.insertMesOnPagamentosMensais(novoMes.id)
                    this@EscolheMesActivity.meses.add(novoMes)
                }
                operation.await()
                (this@EscolheMesActivity.lvEscolha.adapter as EscolhaAdapter).notifyDataSetChanged()
            }
        }
    }

    inner class OnItemCLickLista : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            this@EscolheMesActivity?.log("meses sao ${meses.get(position)}")

            launch {
                this?.let {
                    var mes : Mes = Mes(-1, -1, null)

                    var operation = async {
                        mes = repository.findMesById(meses.get(position).id)
                    }
                    operation.await()
                    val it = Intent()
                    it.putExtra("MES", mes)

                    setResult(Activity.RESULT_OK, it)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}


