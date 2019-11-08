package com.example.projeto

import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.viewpager.widget.ViewPager
import com.example.projeto.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    var MAIN_TAB_PAGE = 0
    private lateinit var aviaoReceiver: AviaoReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        this.aviaoReceiver = AviaoReceiver()

        //Notification result
        var resultIt : Int? = intent.extras?.getInt("DIARIO")

        if(resultIt != null){
            MAIN_TAB_PAGE = resultIt
        }

        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(MAIN_TAB_PAGE)?.select()

        emitirNotificacao()
    }

    override fun onResume() {
        super.onResume()

        //Registrar o receiver
        val itfAviao = IntentFilter()
        itfAviao.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(this.aviaoReceiver, itfAviao)

    }

    override fun onPause() {
        super.onPause()

        //Desregistrar
        unregisterReceiver(this.aviaoReceiver)
    }


    //Notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun criarNotificationChannel(id: String, name: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun emitirNotificacao() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            criarNotificationChannel("CHANNEL_1", "Canal de exemplo", NotificationManager.IMPORTANCE_DEFAULT)
        }

        val notification = NotificationCompat.Builder(this, "CHANNEL_1")

        notification
            .setSmallIcon(R.drawable.ic_notification_smw)
            .setContentTitle("Hooooraay!!!")
            .setContentText("Clique aqui para ir para seu registro diário!")
            .setNumber(3)
            .setAutoCancel(true)
            .setContentIntent(getActivityIntent())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, notification.build())
    }

    @TargetApi(20)
    fun getActivityIntent()  : PendingIntent{

        val resultIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        resultIntent.putExtra("DIARIO", 1)

        val resultPendingIntent : PendingIntent? = TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        return resultPendingIntent!!
    }
}
