package com.example.projeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private lateinit var handler : Handler
    private var splashTime : Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.handler = Handler()
        this.handler.postDelayed({
            goToMainActivity()
        }, this.splashTime)
    }

    fun goToMainActivity() {
        val it = Intent(this, FloatingActivity::class.java)
//        val it = Intent(this, MainActivity::class.java)
        startActivity(it)
        finish()
    }
}
