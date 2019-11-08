package com.example.projeto.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.projeto.*
import com.example.projeto.adapters.ContaAdapter
import com.example.projeto.db.database.AppDatabase
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.entities.User
import com.example.projeto.db.repository.Repository
import com.example.projeto.helpers.log
import com.example.projeto.helpers.toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dialog_mes.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    private lateinit var tvMes : TextView
    private lateinit var repository : Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var layout = inflater.inflate(R.layout.activity_floating, container, false)
        val fab: FloatingActionButton = layout.findViewById(R.id.fab)

        this.listaContas = layout.findViewById(R.id.lvMainContas)
        this.tvMes = layout.findViewById(R.id.tvMainMes)

        fab.setOnClickListener { view ->
            this.goToCadastrarConta()
        }

        //DB stuff
        launch {
            context?.let {
                //Instanciar o repositorio
                repository = Repository(AppDatabase.getDatabase(context!!).UserDao(),AppDatabase.getDatabase(context!!).MesDao(), AppDatabase.getDatabase(context!!).PagamentoDao())

//                debugarBanco() // Fins de testes apenas - TODO: deletar esta linha
//                cadastraDadosBanco() // Fins de testes apenas - TODO: deletar esta linha
//                eliminaDadosBanco() // Fins de testes apenas - TODO: deletar esta linha


                var users : List<User> = repository.getAllUsers()
                if(users.size > 0) {
                    mesAtual = repository.getLastMes()
                    pagamentos = repository.findPagamentosMensaisByMes(mesAtual.id) as ArrayList<Pagamento>
                    var user : User = repository.findUserByName("Samuel")
                    meses = repository.findMesesByUser(user.id) as ArrayList<Mes>
                    tvMes.text = "${mesAtual.nome} ${mesAtual.ano}"

                    listaContas.adapter = getContext()?.let {
                        ContaAdapter(
                            it,
                            pagamentos
                        )
                    }
                } else {
//                    insereUsuarioEMes() // Fins de testes apenas - TODO: deletar esta linha
                }
            }
        }

        tvMes.setOnClickListener(escolhaMes())

        this.listaContas.setOnItemClickListener(OnClickLista())
        this.listaContas.setOnItemLongClickListener(OnItemLongClickLista())

        return layout
    }

    fun goToCadastrarConta() {
        val it = Intent(context, CadastrarActivity::class.java)
        startActivityForResult(it, FORM_CADASTRAR)
    }

    fun atualizaLista() {
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

    private fun Double.format(digits: Int): String {
        return "%.${digits}f".format(this)
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
            var pagamento : Pagamento = pagamentos.get(position)

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

    suspend fun debugarBanco() {

        var users : List<User> = repository.getAllUsers()
        var meses : List<Mes> = repository.getAllMeses()
        var pagamentos : List<Pagamento> = repository.getAllPagamentos()

        for(user in users) {
            context?.log("User: Id - ${user.id} / ${user.nome}")
        }

        for(mes in meses) {
            context?.log("Mes: Id - ${mes.id} / ${mes.num_mes} / ${mes.nome} / ${mes.ano} / user: ${mes.user}")
        }

        for(pag in pagamentos) {
            context?.log("Pagamento: Id - ${pag.id} / ${pag.nome} / ${pag.dataPagamento} / mes: ${pag.mes}")
        }
    }

    suspend fun eliminaDadosBanco() {
        pagamentos = repository.getAllPagamentos() as ArrayList<Pagamento>

//        for(i in pagamentos) {
//            if(i.mes == 5) {
//                repository.deletePagamentos(i)
//
//            }else if(i.mes == 6) {
//                repository.deletePagamentos(i)
//            }else if(i.mes == 7){
//                repository.deletePagamentos(i)
//            }else if(i.mes == 8){
//                repository.deletePagamentos(i)
//
//            }
//
//        }

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

    suspend fun cadastraDadosBanco() {

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


}

