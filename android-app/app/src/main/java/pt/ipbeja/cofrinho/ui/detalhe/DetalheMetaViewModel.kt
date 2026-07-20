package pt.ipbeja.cofrinho.ui.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pt.ipbeja.cofrinho.data.MetaRepository
import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.MetaComTotal

class DetalheMetaViewModel(
    private val repository: MetaRepository,
    private val metaId: Long
) : ViewModel() {

    val meta: StateFlow<MetaComTotal?> =
        repository.observarMeta(metaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val depositos: StateFlow<List<Deposito>> =
        repository.observarDepositos(metaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun adicionarDeposito(valor: Double, nota: String) {
        if (valor <= 0.0) return
        viewModelScope.launch {
            repository.adicionarDeposito(
                Deposito(metaId = metaId, valor = valor, nota = nota.trim())
            )
        }
    }

    fun eliminarDeposito(deposito: Deposito) {
        viewModelScope.launch { repository.eliminarDeposito(deposito) }
    }
}
