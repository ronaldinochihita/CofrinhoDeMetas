package pt.ipbeja.cofrinho.ui.common

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val LOCALE_PT = Locale("pt", "PT")
private val moedaFmt: NumberFormat = NumberFormat.getCurrencyInstance(LOCALE_PT)
private val dataFmt = SimpleDateFormat("dd/MM/yyyy", LOCALE_PT)
private val dataHoraFmt = SimpleDateFormat("dd/MM/yyyy HH:mm", LOCALE_PT)

fun formatarMoeda(valor: Double): String = moedaFmt.format(valor)

fun formatarData(millis: Long?): String =
    if (millis == null) "Sem prazo" else dataFmt.format(Date(millis))

fun formatarDataHora(millis: Long): String = dataHoraFmt.format(Date(millis))

fun diasRestantes(millis: Long?): Long? {
    if (millis == null) return null
    val diff = millis - System.currentTimeMillis()
    return diff / (24L * 60 * 60 * 1000)
}
