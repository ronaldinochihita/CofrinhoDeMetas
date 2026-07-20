package pt.ipbeja.cofrinho.data.entity

import androidx.room.Embedded

data class MetaComTotal(
    @Embedded val meta: Meta,
    val totalDepositado: Double,
    val numDepositos: Int
) {
    val progresso: Float
        get() = if (meta.valorObjetivo <= 0.0) 0f
                else (totalDepositado / meta.valorObjetivo).toFloat().coerceIn(0f, 1f)

    val emFalta: Double
        get() = (meta.valorObjetivo - totalDepositado).coerceAtLeast(0.0)

    val concluida: Boolean
        get() = totalDepositado >= meta.valorObjetivo && meta.valorObjetivo > 0.0
}
