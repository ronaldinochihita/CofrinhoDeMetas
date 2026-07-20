package pt.ipbeja.cofrinho.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deposito",
    foreignKeys = [
        ForeignKey(
            entity = Meta::class,
            parentColumns = ["id"],
            childColumns = ["metaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("metaId")]
)
data class Deposito(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val metaId: Long,
    val valor: Double,
    val data: Long = System.currentTimeMillis(),
    val nota: String = ""
)
