package pt.ipbeja.cofrinho.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meta")
data class Meta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val valorObjetivo: Double,
    val dataLimite: Long? = null,
    val corHex: String = "#4CAF50",
    val iconeChave: String = "savings",
    val dataCriacao: Long = System.currentTimeMillis()
)
