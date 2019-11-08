package com.example.projeto.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projeto.db.dao.MesDao
import com.example.projeto.db.dao.PagamentoDao
import com.example.projeto.db.dao.UserDao
import com.example.projeto.db.entities.Mes
import com.example.projeto.db.entities.Pagamento
import com.example.projeto.db.entities.User

@Database(entities = arrayOf(Mes::class, Pagamento::class, User::class), version = 3)
abstract class AppDatabase : RoomDatabase(){

    abstract fun UserDao() : UserDao
    abstract fun MesDao() : MesDao
    abstract fun PagamentoDao() : PagamentoDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
