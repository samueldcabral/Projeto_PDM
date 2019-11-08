package com.example.projeto.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "mes_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id")
    ))
)
class Mes (
    var num_mes : Int,
    var ano : Int,
    @ColumnInfo(name = "user_id")
    var user : Int?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    var nome : String = ""
}