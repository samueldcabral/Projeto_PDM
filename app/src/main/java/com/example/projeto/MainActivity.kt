package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.viewpager.widget.ViewPager
import com.example.projeto.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    var MAIN_TAB_PAGE = 1
//    private lateinit var listaContas : ListView
//    private var contas : ArrayList<String> = arrayListOf("1", "2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(MAIN_TAB_PAGE)?.select()
//        this.listaContas = findViewById(R.id.lvMainContas)
//
//        this.listaContas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.contas)

    }

//    fun cadastraContas() {
//        this.contas.add("Aluguel")
//        this.contas.add("Agua")
//        this.contas.add("Energia")
//        this.contas.add("Internet")
//    }
}
