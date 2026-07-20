package pt.ipbeja.cofrinho.ui.metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pt.ipbeja.cofrinho.data.MetaRepository
import pt.ipbeja.cofrinho.data.entity.Meta
import pt.ipbeja.cofrinho.data.entity.MetaComTotal

class MetasListViewModel(
    private val repository: MetaRepository
) : ViewModel() {

    private val _pesquisa = MutableStateFlow("")
    val pesquisa: StateFlow<String> = _pesquisa.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val metas: StateFlow<List<MetaComTotal>> =
        _pesquisa
            .flatMapLatest { termo -> repository.pesquisarMetas(termo) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun atualizarPesquisa(novo: String) { _pesquisa.value = novo }

    fun eliminarMeta(meta: Meta) {
        viewModelScope.launch { repository.eliminarMeta(meta) }
    }
}
