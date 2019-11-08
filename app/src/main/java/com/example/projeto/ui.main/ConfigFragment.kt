package com.example.projeto.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.projeto.AviaoReceiver
import com.example.projeto.R

class ConfigFragment : BaseFragment(){
    private lateinit var aviaoReceiver: AviaoReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var layout = inflater.inflate(R.layout.fragment_diario, container, false)
//
//        var imAviao = layout.findViewById<ImageView>(R.id.ivFunFunAviao)
//        var imPanda = layout.findViewById<ImageView>(R.id.ivFunFunPanda)
//
//
//
//        val anim_aviao = AnimationUtils.loadAnimation(context, R.anim.anim_aviao)
//
//        imAviao.startAnimation(anim_aviao)


        return inflater.inflate(R.layout.fragment_config, container, false)
    }
}