package com.example.projeto.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "user_table"
)
class User (
            var nome : String
                                ) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}