package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class FunActivity : AppCompatActivity() {
    private lateinit var handler : Handler
    private var splashTime : Long = 6000
    private lateinit var ivAviao : ImageView
    private lateinit var ivPanda : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun)

        this.ivAviao = findViewById(R.id.ivFunFunAviao)
        this.ivPanda = findViewById(R.id.ivFunFunPanda)
        val anim1 = AnimationUtils.loadAnimation(this, R.anim.anim_aviao)
        val anim2 = AnimationUtils.loadAnimation(this, R.anim.anim_panda)

        this.ivAviao.startAnimation(anim1)
        this.ivPanda.startAnimation(anim2)

        this.handler = Handler()
        this.handler.postDelayed({
            goToMainActivity()
        }, this.splashTime)
    }

    fun goToMainActivity() {
        val it = Intent(this, MainActivity::class.java)
        startActivity(it)
        finish()
    }
}
