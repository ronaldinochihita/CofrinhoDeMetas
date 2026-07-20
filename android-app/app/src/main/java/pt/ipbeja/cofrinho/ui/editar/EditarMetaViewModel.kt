package pt.ipbeja.cofrinho.ui.editar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.ipbeja.cofrinho.data.MetaRepository
import pt.ipbeja.cofrinho.data.entity.Meta

data class EditarMetaUiState(
    val id: Long = 0L,
    val nome: String = "",
    val valorObjetivoTexto: String = "",
    val dataLimite: Long? = null,
    val corHex: String = "#4CAF50",
    val iconeChave: String = "savings",
    val carregado: Boolean = false,
    val erroNome: Boolean = false,
    val erroValor: Boolean = false
) {
    val isEdicao: Boolean get() = id != 0L
}

class EditarMetaViewModel(
    private val repository: MetaRepository,
    private val metaId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarMetaUiState())
    val uiState: StateFlow<EditarMetaUiState> = _uiState.asStateFlow()

    init { carregar() }

    private fun carregar() {
        if (metaId == 0L) {
            _uiState.update { it.copy(carregado = true) }
            return
        }
        viewModelScope.launch {
            val existente = repository.obterMeta(metaId) ?: return@launch
            _uiState.value = EditarMetaUiState(
                id = existente.id,
                nome = existente.nome,
                valorObjetivoTexto = existente.valorObjetivo.toString(),
                dataLimite = existente.dataLimite,
                corHex = existente.corHex,
                iconeChave = existente.iconeChave,
                carregado = true
            )
        }
    }

    fun mudarNome(v: String) = _uiState.update { it.copy(nome = v, erroNome = false) }
    fun mudarValor(v: String) = _uiState.update { it.copy(valorObjetivoTexto = v, erroValor = false) }
    fun mudarData(v: Long?) = _uiState.update { it.copy(dataLimite = v) }
    fun mudarCor(v: String) = _uiState.update { it.copy(corHex = v) }
    fun mudarIcone(v: String) = _uiState.update { it.copy(iconeChave = v) }

    fun guardar(aoTerminar: (Long) -> Unit) {
        val s = _uiState.value
        val valor = s.valorObjetivoTexto.replace(',', '.').toDoubleOrNull()
        val erroNome = s.nome.isBlank()
        val erroValor = valor == null || valor <= 0.0
        if (erroNome || erroValor) {
            _uiState.update { it.copy(erroNome = erroNome, erroValor = erroValor) }
            return
        }
        val meta = Meta(
            id = s.id,
            nome = s.nome.trim(),
            valorObjetivo = valor!!,
            dataLimite = s.dataLimite,
            corHex = s.corHex,
            iconeChave = s.iconeChave
        )
        viewModelScope.launch {
            val id = repository.guardarMeta(meta)
            aoTerminar(id)
        }
    }

    fun eliminar(aoTerminar: () -> Unit) {
        val s = _uiState.value
        if (s.id == 0L) return
        viewModelScope.launch {
            val meta = repository.obterMeta(s.id) ?: return@launch
            repository.eliminarMeta(meta)
            aoTerminar()
        }
    }
}
