package pt.ipbeja.cofrinho.data

import kotlinx.coroutines.flow.Flow
import pt.ipbeja.cofrinho.data.dao.DepositoDao
import pt.ipbeja.cofrinho.data.dao.MetaDao
import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.Meta
import pt.ipbeja.cofrinho.data.entity.MetaComTotal

class MetaRepository(
    private val metaDao: MetaDao,
    private val depositoDao: DepositoDao
) {

    fun observarMetasComTotais(): Flow<List<MetaComTotal>> = metaDao.observarComTotais()

    fun observarMeta(id: Long): Flow<MetaComTotal?> = metaDao.observarComTotalPorId(id)

    fun observarDepositos(metaId: Long): Flow<List<Deposito>> =
        depositoDao.observarPorMeta(metaId)

    fun observarTotalGlobal(): Flow<Double> = depositoDao.observarTotalGlobal()

    fun pesquisarMetas(termo: String): Flow<List<MetaComTotal>> =
        if (termo.isBlank()) metaDao.observarComTotais() else metaDao.pesquisar(termo)

    suspend fun obterMeta(id: Long): Meta? = metaDao.obterPorId(id)

    suspend fun guardarMeta(meta: Meta): Long =
        if (meta.id == 0L) metaDao.inserir(meta) else { metaDao.atualizar(meta); meta.id }

    suspend fun eliminarMeta(meta: Meta) = metaDao.eliminar(meta)

    suspend fun adicionarDeposito(deposito: Deposito): Long = depositoDao.inserir(deposito)

    suspend fun atualizarDeposito(deposito: Deposito) = depositoDao.atualizar(deposito)

    suspend fun eliminarDeposito(deposito: Deposito) = depositoDao.eliminar(deposito)
}
