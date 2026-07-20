package pt.ipbeja.cofrinho.ui.detalhe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.ipbeja.cofrinho.data.entity.Deposito
import pt.ipbeja.cofrinho.data.entity.MetaComTotal
import pt.ipbeja.cofrinho.ui.common.PreviewData
import pt.ipbeja.cofrinho.ui.common.diasRestantes
import pt.ipbeja.cofrinho.ui.common.emojiPorChave
import pt.ipbeja.cofrinho.ui.common.formatarData
import pt.ipbeja.cofrinho.ui.common.formatarDataHora
import pt.ipbeja.cofrinho.ui.common.formatarMoeda
import pt.ipbeja.cofrinho.ui.common.hexParaCor
import pt.ipbeja.cofrinho.ui.theme.CofrinhoDeMetasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheMetaScreen(
    vm: DetalheMetaViewModel,
    aoVoltar: () -> Unit,
    aoEditar: () -> Unit
) {
    val meta by vm.meta.collectAsStateWithLifecycle()
    val depositos by vm.depositos.collectAsStateWithLifecycle()
    var mostrarDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(meta?.meta?.nome ?: "Detalhe da meta") },
                navigationIcon = {
                    IconButton(onClick = aoVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = aoEditar) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarDialog = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Adicionar depósito") }
            )
        }
    ) { padding ->
        val m = meta
        if (m == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("A carregar…")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { CartaoResumo(m) }
                item {
                    Text(
                        "Depósitos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (depositos.isEmpty()) {
                    item {
                        Text(
                            "Ainda não há depósitos. Carrega em \"Adicionar depósito\" abaixo.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(depositos, key = { it.id }) { dep ->
                        LinhaDeposito(dep, aoEliminar = { vm.eliminarDeposito(dep) })
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (mostrarDialog) {
        DialogNovoDeposito(
            aoFechar = { mostrarDialog = false },
            aoConfirmar = { valor, nota ->
                vm.adicionarDeposito(valor, nota)
                mostrarDialog = false
            }
        )
    }
}

@Composable
private fun CartaoResumo(m: MetaComTotal) {
    val cor = hexParaCor(m.meta.corHex)
    val dias = diasRestantes(m.meta.dataLimite)
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(cor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emojiPorChave(m.meta.iconeChave),
                        fontSize = 28.sp
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(m.meta.nome, style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Objetivo: ${formatarMoeda(m.meta.valorObjetivo)}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { m.progresso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = cor,
                trackColor = cor.copy(alpha = 0.15f)
            )
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Poupado: ${formatarMoeda(m.totalDepositado)}",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    if (m.concluida) "Concluída" else "Falta: ${formatarMoeda(m.emFalta)}",
                    color = if (m.concluida) cor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.EventAvailable,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Prazo: ${formatarData(m.meta.dataLimite)}" +
                            (dias?.let { d ->
                                if (d < 0) "  •  Prazo passado" else "  •  Faltam $d dias"
                            } ?: ""),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LinhaDeposito(dep: Deposito, aoEliminar: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    formatarMoeda(dep.valor),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatarDataHora(dep.data) +
                            if (dep.nota.isNotBlank()) "  •  ${dep.nota}" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = aoEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar depósito",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun DialogNovoDeposito(
    aoFechar: () -> Unit,
    aoConfirmar: (Double, String) -> Unit
) {
    var valorTxt by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    val valor = valorTxt.replace(',', '.').toDoubleOrNull()
    val valido = valor != null && valor > 0.0

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Novo depósito") },
        text = {
            Column {
                OutlinedTextField(
                    value = valorTxt,
                    onValueChange = { valorTxt = it },
                    label = { Text("Valor (€)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    label = { Text("Nota (opcional)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (valido) aoConfirmar(valor!!, nota) },
                enabled = valido
            ) { Text("Adicionar") }
        },
        dismissButton = { TextButton(onClick = aoFechar) { Text("Cancelar") } }
    )
}

// ------------------------------------------------------------
// @Preview — visualização no Design view do Android Studio
// ------------------------------------------------------------

@Preview(name = "Cartão de resumo — em progresso", showBackground = true,
         widthDp = 380)
@Composable
private fun PreviewCartaoResumoEmProgresso() {
    CofrinhoDeMetasTheme {
        Column(Modifier.padding(16.dp)) {
            CartaoResumo(m = PreviewData.viagemComTotal)
        }
    }
}

@Preview(name = "Cartão de resumo — concluído", showBackground = true,
         widthDp = 380)
@Composable
private fun PreviewCartaoResumoConcluido() {
    CofrinhoDeMetasTheme {
        Column(Modifier.padding(16.dp)) {
            CartaoResumo(
                m = PreviewData.viagemComTotal.copy(totalDepositado = 500.0)
            )
        }
    }
}

@Preview(name = "Linha de depósito", showBackground = true,
         widthDp = 380)
@Composable
private fun PreviewLinhaDeposito() {
    CofrinhoDeMetasTheme {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PreviewData.depositosViagem.forEach { dep ->
                LinhaDeposito(dep = dep, aoEliminar = {})
            }
        }
    }
}

@Preview(name = "Diálogo — novo depósito", showBackground = true)
@Composable
private fun PreviewDialogNovoDeposito() {
    CofrinhoDeMetasTheme {
        DialogNovoDeposito(aoFechar = {}, aoConfirmar = { _, _ -> })
    }
}
