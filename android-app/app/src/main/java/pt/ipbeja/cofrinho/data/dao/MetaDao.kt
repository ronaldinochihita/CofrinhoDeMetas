package pt.ipbeja.cofrinho.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pt.ipbeja.cofrinho.data.entity.Meta
import pt.ipbeja.cofrinho.data.entity.MetaComTotal

@Dao
interface MetaDao {

    @Query("SELECT * FROM meta ORDER BY dataCriacao DESC")
    fun observarTodas(): Flow<List<Meta>>

    @Query("""
        SELECT m.*,
               IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
               COUNT(d.id)               AS numDepositos
        FROM meta m
        LEFT JOIN deposito d ON d.metaId = m.id
        GROUP BY m.id
        ORDER BY m.dataCriacao DESC
    """)
    fun observarComTotais(): Flow<List<MetaComTotal>>

    @Query("""
        SELECT m.*,
               IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
               COUNT(d.id)               AS numDepositos
        FROM meta m
        LEFT JOIN deposito d ON d.metaId = m.id
        WHERE m.id = :id
        GROUP BY m.id
    """)
    fun observarComTotalPorId(id: Long): Flow<MetaComTotal?>

    @Query("SELECT * FROM meta WHERE id = :id")
    suspend fun obterPorId(id: Long): Meta?

    @Query("""
        SELECT m.*,
               IFNULL(SUM(d.valor), 0.0) AS totalDepositado,
               COUNT(d.id)               AS numDepositos
        FROM meta m
        LEFT JOIN deposito d ON d.metaId = m.id
        WHERE m.nome LIKE '%' || :termo || '%'
        GROUP BY m.id
        ORDER BY m.dataCriacao DESC
    """)
    fun pesquisar(termo: String): Flow<List<MetaComTotal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(meta: Meta): Long

    @Update
    suspend fun atualizar(meta: Meta)

    @Delete
    suspend fun eliminar(meta: Meta)

    @Query("SELECT COUNT(*) FROM meta")
    suspend fun contar(): Int
}
