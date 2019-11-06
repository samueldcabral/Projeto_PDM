package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    private lateinit var handler : Handler
    private var splashTime : Long = 2500
    private lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim1 = AnimationUtils.loadAnimation(this, R.anim.anim1)
        this.image = findViewById(R.id.ivSplashLogo)
        this.image.startAnimation(anim1)

        this.handler = Handler()
        this.handler.postDelayed({
            goToMainActivity()
        }, this.splashTime)
    }

    fun goToMainActivity() {
        val it = Intent(this, MainActivity::class.java)
//        val it = Intent(this, MainActivity::class.java)
        startActivity(it)
        finish()
    }
}
