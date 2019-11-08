package com.example.projeto

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity

class AviaoReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val status = intent?.getBooleanExtra("state", false)

        var inflater = LayoutInflater.from(context)
        var layout = inflater.inflate(R.layout.fragment_config, null)
        var ivAviao : ImageView = layout.findViewById(R.id.ivFunFunAviao)
        var ivPanda = layout.findViewById<ImageView>(R.id.ivFunFunPanda)

        val anim_aviao = AnimationUtils.loadAnimation(context, R.anim.anim_aviao)
        val anim1 = AnimationUtils.loadAnimation(context, R.anim.anim1)


        if(status!!) {
            val it = Intent(context, FunActivity::class.java)
            startActivity(context!!,it,null)

        } else {
            context?.toast("Des ")
        }
    }
}