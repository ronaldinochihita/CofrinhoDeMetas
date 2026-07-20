package pt.ipbeja.cofrinho

import android.app.Application
import pt.ipbeja.cofrinho.data.AppDatabase
import pt.ipbeja.cofrinho.data.MetaRepository

class CofrinhoApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.get(this) }
    val repository: MetaRepository by lazy {
        MetaRepository(database.metaDao(), database.depositoDao())
    }
}
