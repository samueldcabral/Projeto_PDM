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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.projeto.*
//import com.example.projeto.FloatingActivity
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.entities.User
import com.example.projeto.db.repository.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_escolha_mes.view.*
import kotlinx.android.synthetic.main.dialog_mes.*
import kotlinx.android.synthetic.main.dialog_mes.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*

class AppMainFragment : BaseFragment() {
    private var FORM_CADASTRAR = 1
    private var FORM_ESCOLHE = 2
    private var ID_USUARIO_PADRAO = -1
    private var ULTIMO_ID = -1
    private var CHANNEL_ID = "notification"
    private var notificationId = 1
    private var TIPO_PAGAMENTO = "Mensal"
    private lateinit var mesAtual : Mes
    private lateinit var ultimoMesCadastrado : Mes
    private lateinit var listaContas : ListView
    private lateinit var pagamentos : ArrayList<Pagamento>
    private lateinit var meses : ArrayList<Mes>
//    private lateinit var user : User
    private lateinit var tvMes : TextView
    private lateinit var repository : Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var layout = inflater.inflate(R.layout.activity_floating, container, false)
        val fab: FloatingActionButton = layout.findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            this.showNotification()
            context?.log("What the actual fuck")
            this.goToCadastrarConta()

        }

        this.listaContas = layout.findViewById(R.id.lvMainContas)
        this.tvMes = layout.findViewById(R.id.tvMainMes)
        //DB stuff

        launch {
            context?.let {
                repository = Repository(AppDatabase.getDatabase(context!!).UserDao(),AppDatabase.getDatabase(context!!).MesDao(), AppDatabase.getDatabase(context!!).PagamentoDao())

                // SELECT * FROM user pag mes

//                var users : List<User> = repository.getAllUsers()
//                var meses : List<Mes> = repository.getAllMeses()
//                var pagamentos : List<Pagamento> = repository.getAllPagamentos()
//////
//////
//                for(user in users) {
//                    Log.i("APP_PROJETO", "Repository: the users are id: ${user.id} - ${user.nome}")
//                }
//////////
//                for(mes in meses) {
//                    Log.i("APP_PROJETO", "Repository: the meses are id: ${mes.id} - num_mes: ${mes.num_mes} nome: ${mes.nome} ano: ${mes.ano} user: ${mes.user}")
//                }
//////
//                for(pag in pagamentos) {
//                    Log.i("APP_PROJETO", "Repository: the pagamentos are id: ${pag.id} - nome: ${pag.nome} dataPagamento: ${pag.dataPagamento} mes: ${pag.mes}")
//                }
//
//                Log.i("APP_PROJETO","CACETAAAAAAAAAAAAAAAAAAAA")
//                 END SELECT



//                cadastraContas()
//                elimina()
                var users : List<User> = repository.getAllUsers()
                if(users.size > 0) {
                    mesAtual = repository.getLastMes()
                    pagamentos = repository.findPagamentosMensaisByMes(mesAtual.id) as ArrayList<Pagamento>
                    var user : User = repository.findUserByName("Samuel")
                    meses = repository.findMesesByUser(user.id) as ArrayList<Mes>
                    tvMes.text = "${mesAtual.nome} ${mesAtual.ano}"
                    listaContas.adapter = getContext()?.let { ContaAdapter(it, pagamentos) }
                } else {
                    insereUsuarioEMes()
                }



            }
        } //launch


        tvMes.setOnClickListener(escolhaMes())

        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())

        return layout
    }


    inner class escolhaMes : View.OnClickListener {
        override fun onClick(v: View?) {
            launch {
                var user : User = repository.findUserByName("Samuel")
                var mesesNovos = repository.findMesesByUser(user.id) as ArrayList<Mes>
                val it = Intent(context, EscolheMesActivity::class.java)
                it.putExtra("MESES", mesesNovos)
                startActivityForResult(it, FORM_ESCOLHE)
            }


            /*
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_escolha_mes, null)


            mDialogView.lvDialogEscolhaLista.adapter = getContext()?.let{
                EscolhaAdapter(it, meses)
            }

            mDialogView.lvDialogEscolhaLista.setOnItemClickListener(OnEscolhaClick())

            val mDialogBuilder = AlertDialog.Builder(context)
            mDialogBuilder.setTitle("Escolha o mes")
            mDialogBuilder.setView(mDialogView)
            mDialogBuilder.create()

            var mAlertDialog = mDialogBuilder.show()

            mDialogView.btDialogEscolhaAdicionar.setOnClickListener{
                mAlertDialog.dismiss()
                launch {
                    var operation = async {
                        var novoMes = repository.insertNovoMes()
                        meses.add(novoMes)
                    }
                   operation.await()
                    (mDialogView.lvDialogEscolhaLista.adapter as EscolhaAdapter).notifyDataSetChanged()
                }
            }
        }
        */
            /*
        inner class OnEscolhaClick : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                launch {
                    context?.let {
                        var mes = repository.findMesById(meses.get(position).id)
                        context?.log("mesatual ${mes.num_mes} ${mes.nome} ${mes.id}")

                        this@AppMainFragment.mesAtual = mes
                        this@AppMainFragment.pagamentos.clear()
//                        this@AppMainFragment.pagamentos = repository.findPagamentosByMes(mesAtual.id) as ArrayList<Pagamento>
                        this@AppMainFragment.pagamentos = arrayListOf(Pagamento("teste", "um", false, 4.4, null,null,null,null))
                        context?.log("pagamentos ${pagamentos.get(0).nome}")
                        this@AppMainFragment.atualizaLista3()
                    }

                }

            }

        }
        */
        }
    }

    suspend fun elimina() {
        pagamentos = repository.getAllPagamentos() as ArrayList<Pagamento>

        for(i in pagamentos) {
            if(i.mes ==5) {
                repository.deletePagamentos(i)

            }else if(i.mes == 6) {
                repository.deletePagamentos(i)
            }

        }

        meses = repository.getAllMeses() as ArrayList<Mes>
        for(i in meses) {
            if(i.ano > 2019){
                repository.deleteMes(i)

            }

        }

//        var user = repository.getAllUsers()
//        for(i in user) {
//            repository.deleteUser(i)
//        }

    }

    suspend fun insereUsuarioEMes() {
        val userDb : User = User("Samuel")
        val insertedUser = repository.insertUser(userDb)
        ID_USUARIO_PADRAO = insertedUser.toInt()

        val mesDb : Mes = Mes(12, 2019, insertedUser.toInt())
        val insertedMes = repository.insertMes(mesDb)
    }

    suspend fun cadastraContas() {

        val userDb : User = User("Samuel")
        val insertedUser = repository.insertUser(userDb)


//        val mesDb : Mes = Mes(12, 2019, ID_USUARIO_PADRAO)
        val mesDb : Mes = Mes(12, 2019, insertedUser.toInt())
        val insertedMes = repository.insertMes(mesDb)

        val pagamentoDbOne : Pagamento = Pagamento("Aluguel", TIPO_PAGAMENTO, false, 500.00, null, Date(), null, insertedMes.toInt())
        val pagamentoDbTwo : Pagamento = Pagamento("Agua", TIPO_PAGAMENTO, false, 50.00, null, Date(), null, insertedMes.toInt())
        val pagamentoDbThree : Pagamento = Pagamento("Energia", TIPO_PAGAMENTO, false, 100.00, null, null, null, insertedMes.toInt())
        val pagamentoDbFour : Pagamento = Pagamento("Internet", TIPO_PAGAMENTO, false, 80.00, null, null, null, insertedMes.toInt())


        repository.insertPagamentos(pagamentoDbOne)
        repository.insertPagamentos(pagamentoDbTwo)
        repository.insertPagamentos(pagamentoDbThree)
        repository.insertPagamentos(pagamentoDbFour)
    }

    fun goToCadastrarConta() {
        val it = Intent(context, CadastrarActivity::class.java)
        startActivityForResult(it, FORM_CADASTRAR)
    }

    fun showNotification(){
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.smw_icon)
            .setContentTitle("It works")
            .setContentText("Oh my god")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }


    }

    fun atualizaLista() {
        (this.listaContas.adapter as ContaAdapter).update()
    }

    fun atualizaLista2() {
        listaContas.adapter = getContext()?.let { ContaAdapter(it, listOf(Pagamento("Sam 1", "1", false, 1.0, null, null, null, null))) }
    }

    suspend fun atualizaLista3() {
        (this.listaContas.adapter as ContaAdapter).update()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == FORM_CADASTRAR) {
                val pagamento = data?.getSerializableExtra("Pagamento") as Pagamento

                launch {
                    val operation = async {
                        var id = repository.insertPagamentos(pagamento)
                        repository.insertMesOnPagamento(id.toInt(), mesAtual.id)
                        pagamentos.add(pagamento)
                        ULTIMO_ID = id.toInt()

                        context?.toast("Pagamento: ${pagamento.nome} was added")
                    }
                    operation.await()

                    atualizaLista()
                }
            } else if(requestCode == FORM_ESCOLHE){
                val mes = data?.getSerializableExtra("MES") as Mes
                mesAtual = mes
                launch {
                    val operation = async {
                        this@AppMainFragment.tvMes.text = "${mes.nome} ${mes.ano}"
                        this@AppMainFragment.pagamentos.clear()
                        this@AppMainFragment.pagamentos.addAll(repository.findPagamentosMensaisByMes(mes.id) as ArrayList<Pagamento>)
                        (this@AppMainFragment.listaContas.adapter as ContaAdapter).update()
                    }
                    operation.await()

                }
            }
        }
    }

    inner class OnClickLista : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mes, null)
            var pagamento = this@AppMainFragment.pagamentos.get(position)
            mDialogView.etDialogNome.text.append(pagamento.nome)
            mDialogView.etDialogValor.text.append(pagamento.valor.toString())
            mDialogView.swDialogStatus.isChecked = pagamento.status

            if(pagamento.diaDePagamento == null) {
                mDialogView.tvDialogDataPag.text = ""

            }else {
                mDialogView.tvDialogDataPag.text = pagamento.diaDePagamento.toString()

            }

            if(pagamento.obs == null) {
                mDialogView.etDialogObs.text.append("")

            }else {
                mDialogView.etDialogObs.text.append(pagamento.obs)

            }

            if(pagamento.id == 0) {
                pagamento.id = ULTIMO_ID
            } else {
                pagamento.id = pagamento.id
            }

            val mDialogBuilder = AlertDialog.Builder(context)
            mDialogBuilder.setTitle("Detalhes da conta")
            mDialogBuilder.setView(mDialogView)

            var mAlertDialog = mDialogBuilder.show()

            mDialogView.btDialogSalvar.setOnClickListener{
                mAlertDialog.dismiss()

                var valor : String = mDialogView.etDialogValor.text.toString()
                valor = valor.replace(',', '.')
                this@AppMainFragment.pagamentos.get(position).nome = mDialogView.etDialogNome.text.toString()
                this@AppMainFragment.pagamentos.get(position).valor = valor.toDouble()
                this@AppMainFragment.pagamentos.get(position).obs = mDialogView.etDialogObs.text.toString()
                this@AppMainFragment.pagamentos.get(position).status = mDialogView.swDialogStatus.isChecked

                launch {
                    val operation = async {
                        pagamento.nome = mDialogView.etDialogNome.text.toString()
                        var valor : String = mDialogView.etDialogValor.text.toString()
                        valor = valor.replace(',', '.')
                        pagamento.valor = valor.toDouble()
                        pagamento.obs = mDialogView.etDialogObs.text.toString()
                        pagamento.status = mDialogView.swDialogStatus.isChecked
                        pagamento.mes = mesAtual.id
                        repository.updatePagamentos(pagamento)
                    }
                    operation.await()
                    this@AppMainFragment.atualizaLista()

                }

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
//            context?.toast("Long pressed")
            var pagamento : Pagamento = pagamentos.get(position)

//            this@AppMainFragment.mesAtual.removePagamento(position)
            launch {
                val operation = async {
                    this@AppMainFragment.repository.deletePagamentos(pagamento)
                    pagamentos.remove(pagamento)
                    this@AppMainFragment.atualizaLista()
                }
                operation.await()
                context?.toast("${pagamento.nome} deletado(a) com sucesso!")

            }
            return true
        }

    }
}

