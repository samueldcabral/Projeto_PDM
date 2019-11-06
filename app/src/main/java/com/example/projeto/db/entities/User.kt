package com.example.projeto.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.projeto.model.Mes

@Entity(
    tableName = "user_table"
)
class User (
    var nome : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}