package pt.ipbeja.cofrinho.data

import pt.ipbeja.cofrinho.data.dao.DepositoDao
import pt.ipbeja.cofrinho.data.dao.MetaDao
import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.Meta

object SeedData {

    suspend fun preencher(metaDao: MetaDao, depositoDao: DepositoDao) {
        if (metaDao.contar() > 0) return

        val agora = System.currentTimeMillis()
        val dia = 24L * 60 * 60 * 1000

        val idViagem = metaDao.inserir(
            Meta(
                nome = "Viagem a Lisboa",
                valorObjetivo = 400.0,
                dataLimite = agora + 90 * dia,
                corHex = "#1976D2",
                iconeChave = "flight"
            )
        )
        val idPortatil = metaDao.inserir(
            Meta(
                nome = "Portátil novo",
                valorObjetivo = 900.0,
                dataLimite = agora + 180 * dia,
                corHex = "#7B1FA2",
                iconeChave = "computer"
            )
        )
        val idFundo = metaDao.inserir(
            Meta(
                nome = "Fundo de emergência",
                valorObjetivo = 1500.0,
                dataLimite = null,
                corHex = "#388E3C",
                iconeChave = "shield"
            )
        )

        depositoDao.inserir(Deposito(metaId = idViagem, valor = 50.0, data = agora - 20 * dia, nota = "Semana 1"))
        depositoDao.inserir(Deposito(metaId = idViagem, valor = 75.0, data = agora - 10 * dia, nota = "Prémio"))
        depositoDao.inserir(Deposito(metaId = idPortatil, valor = 100.0, data = agora - 30 * dia))
        depositoDao.inserir(Deposito(metaId = idPortatil, valor = 100.0, data = agora - 5 * dia))
        depositoDao.inserir(Deposito(metaId = idFundo, valor = 200.0, data = agora - 45 * dia, nota = "Mês passado"))
        depositoDao.inserir(Deposito(metaId = idFundo, valor = 150.0, data = agora - 15 * dia))
    }
}
