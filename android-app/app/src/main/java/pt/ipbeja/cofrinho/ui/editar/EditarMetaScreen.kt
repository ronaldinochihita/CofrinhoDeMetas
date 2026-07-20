package pt.ipbeja.cofrinho.ui.editar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.ipbeja.cofrinho.ui.common.IconesDisponiveis
import pt.ipbeja.cofrinho.ui.common.formatarData
import pt.ipbeja.cofrinho.ui.common.hexParaCor
import pt.ipbeja.cofrinho.ui.theme.CofrinhoDeMetasTheme
import pt.ipbeja.cofrinho.ui.theme.CoresMeta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarMetaScreen(
    vm: EditarMetaViewModel,
    aoVoltar: () -> Unit,
    aoGuardado: (Long) -> Unit,
    aoEliminado: () -> Unit
) {
    val ui by vm.uiState.collectAsStateWithLifecycle()
    var mostrarPicker by rememberSaveable { mutableStateOf(false) }
    var confirmarEliminar by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (ui.isEdicao) "Editar meta" else "Nova meta") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (ui.isEdicao) {
                        IconButton(onClick = { confirmarEliminar = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar meta")
                        }
                    }
                    IconButton(onClick = { vm.guardar(aoGuardado) }) {
                        Icon(Icons.Filled.Check, contentDescription = "Guardar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        FormularioMeta(
            ui = ui,
            onNome = vm::mudarNome,
            onValor = vm::mudarValor,
            onLimparPrazo = { vm.mudarData(null) },
            onCor = vm::mudarCor,
            onIcone = vm::mudarIcone,
            onAbrirPicker = { mostrarPicker = true },
            onSubmit = { vm.guardar(aoGuardado) },
            modifier = Modifier.padding(padding)
        )
    }

    if (mostrarPicker) {
        val estado = rememberDatePickerState(initialSelectedDateMillis = ui.dataLimite)
        DatePickerDialog(
            onDismissRequest = { mostrarPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    vm.mudarData(estado.selectedDateMillis)
                    mostrarPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = estado) }
    }

    if (confirmarEliminar) {
        DialogConfirmarEliminar(
            nomeMeta = ui.nome,
            aoConfirmar = {
                confirmarEliminar = false
                vm.eliminar(aoEliminado)
            },
            aoCancelar = { confirmarEliminar = false }
        )
    }
}

@Composable
private fun FormularioMeta(
    ui: EditarMetaUiState,
    onNome: (String) -> Unit,
    onValor: (String) -> Unit,
    onLimparPrazo: () -> Unit,
    onCor: (String) -> Unit,
    onIcone: (String) -> Unit,
    onAbrirPicker: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OutlinedTextField(
            value = ui.nome,
            onValueChange = onNome,
            label = { Text("Nome da meta") },
            isError = ui.erroNome,
            supportingText = { if (ui.erroNome) Text("Indica um nome.") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = ui.valorObjetivoTexto,
            onValueChange = onValor,
            label = { Text("Valor objetivo (€)") },
            isError = ui.erroValor,
            supportingText = { if (ui.erroValor) Text("Indica um valor > 0.") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAbrirPicker() }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.EventAvailable,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Prazo", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatarData(ui.dataLimite))
            }
            TextButton(onClick = onLimparPrazo, enabled = ui.dataLimite != null) {
                Text("Limpar")
            }
        }

        Text("Cor", style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(CoresMeta) { hex ->
                val selecionada = ui.corHex.equals(hex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(hexParaCor(hex))
                        .border(
                            width = if (selecionada) 3.dp else 0.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        )
                        .clickable { onCor(hex) }
                )
            }
        }

        Text("Ícone", style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(IconesDisponiveis) { opcao ->
                val selecionado = opcao.chave == ui.iconeChave
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (selecionado) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onIcone(opcao.chave) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opcao.emoji,
                        fontSize = 24.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) { Text(if (ui.isEdicao) "Guardar alterações" else "Criar meta") }
    }
}

@Composable
private fun DialogConfirmarEliminar(
    nomeMeta: String,
    aoConfirmar: () -> Unit,
    aoCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text("Eliminar meta?") },
        text = {
            Text(
                "Vais eliminar \"$nomeMeta\" e todos os depósitos associados. " +
                        "Esta ação não pode ser desfeita."
            )
        },
        confirmButton = {
            Button(onClick = aoConfirmar) { Text("Eliminar") }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar) { Text("Cancelar") }
        }
    )
}

// ------------------------------------------------------------
// @Preview — visualização no Design view do Android Studio
// ------------------------------------------------------------

@Preview(name = "Formulário — Nova meta", showBackground = true,
         widthDp = 360, heightDp = 720)
@Composable
private fun PreviewFormularioNova() {
    CofrinhoDeMetasTheme {
        FormularioMeta(
            ui = EditarMetaUiState(carregado = true),
            onNome = {}, onValor = {}, onLimparPrazo = {},
            onCor = {}, onIcone = {}, onAbrirPicker = {}, onSubmit = {}
        )
    }
}

@Preview(name = "Formulário — Editar meta", showBackground = true,
         widthDp = 360, heightDp = 720)
@Composable
private fun PreviewFormularioEditar() {
    CofrinhoDeMetasTheme {
        FormularioMeta(
            ui = EditarMetaUiState(
                id = 1L,
                nome = "Viagem a Lisboa",
                valorObjetivoTexto = "400",
                dataLimite = System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000,
                corHex = "#1976D2",
                iconeChave = "flight",
                carregado = true
            ),
            onNome = {}, onValor = {}, onLimparPrazo = {},
            onCor = {}, onIcone = {}, onAbrirPicker = {}, onSubmit = {}
        )
    }
}

@Preview(name = "Formulário — Com erros", showBackground = true,
         widthDp = 360, heightDp = 720)
@Composable
private fun PreviewFormularioComErros() {
    CofrinhoDeMetasTheme {
        FormularioMeta(
            ui = EditarMetaUiState(
                nome = "",
                valorObjetivoTexto = "0",
                erroNome = true,
                erroValor = true,
                carregado = true
            ),
            onNome = {}, onValor = {}, onLimparPrazo = {},
            onCor = {}, onIcone = {}, onAbrirPicker = {}, onSubmit = {}
        )
    }
}

@Preview(name = "Diálogo eliminar", showBackground = true)
@Composable
private fun PreviewDialogEliminar() {
    CofrinhoDeMetasTheme {
        DialogConfirmarEliminar(
            nomeMeta = "Viagem a Lisboa",
            aoConfirmar = {},
            aoCancelar = {}
        )
    }
}
