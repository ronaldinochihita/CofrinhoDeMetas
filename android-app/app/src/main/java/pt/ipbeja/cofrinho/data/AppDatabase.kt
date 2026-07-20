package pt.ipbeja.cofrinho.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipbeja.cofrinho.data.dao.DepositoDao
import pt.ipbeja.cofrinho.data.dao.MetaDao
import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.Meta

@Database(
    entities = [Meta::class, Deposito::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun metaDao(): MetaDao
    abstract fun depositoDao(): DepositoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        private val seedScope = CoroutineScope(Dispatchers.IO)

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cofrinho.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seedScope.launch {
                            get(context).also { database ->
                                SeedData.preencher(
                                    database.metaDao(),
                                    database.depositoDao()
                                )
                            }
                        }
                    }
                })
                .build()
    }
}
