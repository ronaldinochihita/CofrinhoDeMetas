package pt.ipbeja.cofrinho.ui.common

import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.Meta
import pt.ipbeja.cofrinho.data.entity.MetaComTotal

/**
 * Dados de exemplo usados exclusivamente pelas @Preview do Android Studio.
 * Não são referenciados em runtime — servem só para o Design view.
 */
object PreviewData {

    private const val UM_DIA = 24L * 60 * 60 * 1000
    private val agora = System.currentTimeMillis()

    val metaViagem = Meta(
        id = 1L,
        nome = "Viagem a Lisboa",
        valorObjetivo = 400.0,
        dataLimite = agora + 90 * UM_DIA,
        corHex = "#1976D2",
        iconeChave = "flight"
    )

    val metaPortatil = Meta(
        id = 2L,
        nome = "Portátil novo",
        valorObjetivo = 900.0,
        dataLimite = agora + 180 * UM_DIA,
        corHex = "#7B1FA2",
        iconeChave = "computer"
    )

    val metaFundo = Meta(
        id = 3L,
        nome = "Fundo de emergência",
        valorObjetivo = 1500.0,
        dataLimite = null,
        corHex = "#388E3C",
        iconeChave = "shield"
    )

    val viagemComTotal = MetaComTotal(
        meta = metaViagem,
        totalDepositado = 125.0,
        numDepositos = 3
    )
    val portatilComTotal = MetaComTotal(
        meta = metaPortatil,
        totalDepositado = 200.0,
        numDepositos = 2
    )
    val fundoComTotal = MetaComTotal(
        meta = metaFundo,
        totalDepositado = 350.0,
        numDepositos = 2
    )

    val listaMetas: List<MetaComTotal> =
        listOf(viagemComTotal, portatilComTotal, fundoComTotal)

    val depositosViagem: List<Deposito> = listOf(
        Deposito(id = 10, metaId = 1L, valor = 75.0,
                 data = agora - 5 * UM_DIA,  nota = "Prémio"),
        Deposito(id = 11, metaId = 1L, valor = 50.0,
                 data = agora - 12 * UM_DIA, nota = "Semana 1"),
        Deposito(id = 12, metaId = 1L, valor = 0.0,
                 data = agora - 20 * UM_DIA, nota = "")
    )
}
