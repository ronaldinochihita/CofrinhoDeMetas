package pt.ipbeja.cofrinho.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pt.ipbeja.cofrinho.data.entity.Deposito

@Dao
interface DepositoDao {

    @Query("SELECT * FROM deposito WHERE metaId = :metaId ORDER BY data DESC")
    fun observarPorMeta(metaId: Long): Flow<List<Deposito>>

    @Query("SELECT IFNULL(SUM(valor), 0.0) FROM deposito WHERE metaId = :metaId")
    fun observarTotalPorMeta(metaId: Long): Flow<Double>

    @Query("SELECT IFNULL(SUM(valor), 0.0) FROM deposito")
    fun observarTotalGlobal(): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(deposito: Deposito): Long

    @Update
    suspend fun atualizar(deposito: Deposito)

    @Delete
    suspend fun eliminar(deposito: Deposito)

    @Query("DELETE FROM deposito WHERE metaId = :metaId")
    suspend fun eliminarPorMeta(metaId: Long)
}
