package com.example.projeto.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.projeto.model.Pagamento

@Entity(
    tableName = "mes_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id")
    ))
)
class Mes (
    var nome : String,
    var ano : String,
    @ColumnInfo(name = "user_id")
    var user : Int?
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}