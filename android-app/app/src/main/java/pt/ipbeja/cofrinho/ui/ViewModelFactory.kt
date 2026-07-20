package pt.ipbeja.cofrinho.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.ipbeja.cofrinho.data.MetaRepository
import pt.ipbeja.cofrinho.ui.detalhe.DetalheMetaViewModel
import pt.ipbeja.cofrinho.ui.editar.EditarMetaViewModel
import pt.ipbeja.cofrinho.ui.metas.MetasListViewModel

class ViewModelFactory(
    private val repository: MetaRepository,
    private val metaId: Long = 0L
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MetasListViewModel::class.java) ->
            MetasListViewModel(repository) as T
        modelClass.isAssignableFrom(DetalheMetaViewModel::class.java) ->
            DetalheMetaViewModel(repository, metaId) as T
        modelClass.isAssignableFrom(EditarMetaViewModel::class.java) ->
            EditarMetaViewModel(repository, metaId) as T
        else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
