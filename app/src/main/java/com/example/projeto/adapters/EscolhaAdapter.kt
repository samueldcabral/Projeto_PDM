package com.example.projeto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto.R
import com.example.projeto.db.entities.Mes

class EscolhaAdapter(var context : Context, var meses : ArrayList<Mes>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var layout : View

        if(convertView == null) {
            var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layout = inflater.inflate(R.layout.escolha_layout, null)
        }else {
            layout = convertView
        }

        val tv = layout.findViewById<TextView>(R.id.tvEscolha)
        tv.text = meses.get(position).nome

        return layout
    }

    override fun getItem(position: Int): Any {
        return this.meses.get(position)
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getCount(): Int {
        return meses.size
    }
}