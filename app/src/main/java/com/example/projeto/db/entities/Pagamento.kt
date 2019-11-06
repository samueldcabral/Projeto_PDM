package com.example.projeto.db.entities

import androidx.room.*
import com.example.projeto.db.dao.Converters
import java.util.*

@Entity(
    tableName = "pagamento_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = Mes::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("mes_id")
    ))
)
@TypeConverters(Converters::class)
class Pagamento (
    var nome : String,
    var tipo : String,
    var status : Boolean = false,
    var valor : Double,
    var diaDePagamento : String?,
    var dataPagamento : Date?,
    var obs : String?,
    @ColumnInfo(name = "mes_id")
    var mes : Int?
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}