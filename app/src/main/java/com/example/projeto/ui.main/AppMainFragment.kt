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
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.repository.Repository
import com.example.projeto.model.Mes
import com.example.projeto.model.Pagamento
import com.example.projeto.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_mes.*
import kotlinx.android.synthetic.main.dialog_mes.view.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*

class AppMainFragment : BaseFragment() {
    private var FORM_CADASTRAR = 1
    private var TIPO_PAGAMENTO = "Mensal"
    private lateinit var mesAtual : Mes
    private lateinit var listaContas : ListView
    private lateinit var user : User
    private lateinit var tvMes : TextView
    private lateinit var repository : Repository

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


//        this.listaContas = layout.findViewById(R.id.lvMainContas)
//        this.tvMes = layout.findViewById(R.id.tvMainMes)
//
//        this.tvMes.text = "${mesAtual.nome} ${mesAtual.ano}"
//        this.tvMes.setOnClickListener(Clicked())
//
//        this.listaContas.adapter = getContext()?.let { ContaAdapter(it, this.mesAtual, this.mesAtual.pagamentos) }
//        this.listaContas.setOnItemClickListener(OnClickLista())
//        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())

//        val cal = Calendar.getInstance()
//        val date = Date("01/01/2020")
//
//        Log.i("APP_PROJETO", "month: ${date.month} date: ${date.date} year: ${date.year} \nhours: ${date.hours} \nday: ${date.day}")

//        Log.i("APP_PROJETO", "Year: ${cal.get(Calendar.YEAR)} Month: ${cal.get(Calendar.MONTH)} Day: ${cal.get(Calendar.DAY_OF_MONTH)} total ${cal} ")

        //DB stuff

        launch {


            context?.let {
//                val result = AppDatabase.getDatabase(context!!).UserDao().insertUser(userDb)

//                Toast.makeText(context, "It worked. The id is ${result.toString()}", Toast.LENGTH_SHORT).show()
//                Toast.makeText(context, "The user is ${users.get(0).nome} Please let it happen", Toast.LENGTH_SHORT).show()

//
                repository = Repository(AppDatabase.getDatabase(context!!).UserDao(),AppDatabase.getDatabase(context!!).MesDao(), AppDatabase.getDatabase(context!!).PagamentoDao())

//                var users : List<com.example.projeto.db.entities.User> = AppDatabase.getDatabase(context!!).UserDao().getAllUsers()
//                var meses : List<com.example.projeto.db.entities.Mes> = AppDatabase.getDatabase(context!!).MesDao().getAllMeses()
                var users : List<com.example.projeto.db.entities.User> = repository.getAllUsers()
                var meses : List<com.example.projeto.db.entities.Mes> = repository.getAllMeses()
                var pagamentos : List<com.example.projeto.db.entities.Pagamento> = repository.getAllPagamentos()

//                repository.insertUserOnMes(2, 2)

                for(user in users) {
                    Log.i("APP_PROJETO", "Repository: the users are id: ${user.id} - ${user.nome}")
                }

                for(mes in meses) {
                    Log.i("APP_PROJETO", "Repository: the meses are id: ${mes.id} - nome: ${mes.nome} ano: ${mes.ano} user: ${mes.user}")
                }

                for(pag in pagamentos) {
//                    repository.insertMesOnPagamento(pag.id, 1)
                    Log.i("APP_PROJETO", "Repository: the meses are id: ${pag.id} - nome: ${pag.nome} dataPagamento: ${pag.dataPagamento} mes: ${pag.mes}")
                }

//                cadastraContas()

//                Log.i("APP_PROJETO", "the mes ${mes.get(0).nome} - ${mes.get(0).id} - ${mes.get(0).user}")
            }
        }

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


    suspend fun cadastraContas() {

//        val userDb : com.example.projeto.db.entities.User = com.example.projeto.db.entities.User("Samuel")
//        val insertedUser = AppDatabase.getDatabase(context!!).UserDao().insertUser(userDb)
//
//
//        val mesDb : com.example.projeto.db.entities.Mes = com.example.projeto.db.entities.Mes("Dezembro", "2019", insertedUser.toInt())
//        val insertedMes = AppDatabase.getDatabase(context!!).MesDao().insertMes(mesDb)
//
//
//
//        val pagamentoDbOne : com.example.projeto.db.entities.Pagamento = com.example.projeto.db.entities.Pagamento("Aluguel", TIPO_PAGAMENTO, false, 500.00, null, Date(), null, null)
//        val pagamentoDbTwo : com.example.projeto.db.entities.Pagamento = com.example.projeto.db.entities.Pagamento("Agua", TIPO_PAGAMENTO, false, 50.00, null, Date(), null, null)
        val pagamentoDbThree : com.example.projeto.db.entities.Pagamento = com.example.projeto.db.entities.Pagamento("Energia", TIPO_PAGAMENTO, false, 100.00, null, null, null, null)
        val pagamentoDbFour : com.example.projeto.db.entities.Pagamento = com.example.projeto.db.entities.Pagamento("Internet", TIPO_PAGAMENTO, false, 80.00, null, null, null, null)

        repository.insertPagamentos(pagamentoDbThree)
        repository.insertPagamentos(pagamentoDbFour)

//        Log.i("APP_PROJETO", pagamentoDbOne.nome.toString())
//        Log.i("APP_PROJETO", pagamentoDbOne.dataPagamento.toString())
//        Log.i("APP_PROJETO", "month: ${pagamentoDbOne.dataPagamento?.month}\ndate: ${pagamentoDbOne.dataPagamento?.date} \nyear: ${pagamentoDbOne.dataPagamento?.year}")
//
//        Log.i("APP_PROJETO", "Teste de pagamento acima esta a data")
//        this.user = User("Samuel")
//        var mes = Mes("Dezembro", "2019")
//        user.addMes(mes)
//        this.mesAtual = mes
//        mes.addPagamento(Pagamento("Aluguel", TIPO_PAGAMENTO, 500.00))
//        mes.addPagamento(Pagamento("Agua", TIPO_PAGAMENTO, 50.00))
//        mes.addPagamento(Pagamento("Energia", TIPO_PAGAMENTO, 100.00))
//        mes.addPagamento(Pagamento("Internet", TIPO_PAGAMENTO, 50.00))
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

