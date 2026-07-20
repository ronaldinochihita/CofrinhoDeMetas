package pt.ipbeja.cofrinho.ui.metas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.ipbeja.cofrinho.data.entity.MetaComTotal
import pt.ipbeja.cofrinho.ui.common.PreviewData
import pt.ipbeja.cofrinho.ui.common.emojiPorChave
import pt.ipbeja.cofrinho.ui.common.formatarMoeda
import pt.ipbeja.cofrinho.ui.common.hexParaCor
import pt.ipbeja.cofrinho.ui.theme.CofrinhoDeMetasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetasListScreen(
    vm: MetasListViewModel,
    aoAbrirMeta: (Long) -> Unit,
    aoCriar: () -> Unit
) {
    val metas by vm.metas.collectAsStateWithLifecycle()
    val pesquisa by vm.pesquisa.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cofrinho de Metas", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = aoCriar,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nova meta") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = pesquisa,
                onValueChange = vm::atualizarPesquisa,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = { Text("Pesquisar meta…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            if (metas.isEmpty()) {
                EstadoVazio(comPesquisa = pesquisa.isNotBlank())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(metas, key = { it.meta.id }) { mct ->
                        CartaoMeta(
                            item = mct,
                            aoClicar = { aoAbrirMeta(mct.meta.id) }
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun CartaoMeta(item: MetaComTotal, aoClicar: () -> Unit) {
    val cor = hexParaCor(item.meta.corHex)
    Surface(
        onClick = aoClicar,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(cor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emojiPorChave(item.meta.iconeChave),
                        fontSize = 22.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.meta.nome, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${formatarMoeda(item.totalDepositado)} de ${formatarMoeda(item.meta.valorObjetivo)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${(item.progresso * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = cor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { item.progresso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = cor,
                trackColor = cor.copy(alpha = 0.15f)
            )
            if (item.concluida) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Meta concluída!",
                    style = MaterialTheme.typography.labelLarge,
                    color = cor
                )
            }
        }
    }
}

@Composable
private fun EstadoVazio(comPesquisa: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Savings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                if (comPesquisa) "Sem resultados para a pesquisa"
                else "Ainda não tens metas.\nCarrega em \"Nova meta\" para começar.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ------------------------------------------------------------
// @Preview — visualização no Design view do Android Studio
// ------------------------------------------------------------

@Preview(name = "Cartão de meta — em progresso", showBackground = true,
         widthDp = 380)
@Composable
private fun PreviewCartaoMetaEmProgresso() {
    CofrinhoDeMetasTheme {
        Column(Modifier.padding(16.dp)) {
            CartaoMeta(item = PreviewData.viagemComTotal, aoClicar = {})
        }
    }
}

@Preview(name = "Cartão de meta — concluída", showBackground = true,
         widthDp = 380)
@Composable
private fun PreviewCartaoMetaConcluida() {
    CofrinhoDeMetasTheme {
        Column(Modifier.padding(16.dp)) {
            CartaoMeta(
                item = PreviewData.viagemComTotal.copy(totalDepositado = 400.0),
                aoClicar = {}
            )
        }
    }
}

@Preview(name = "Lista completa", showBackground = true,
         widthDp = 380, heightDp = 720)
@Composable
private fun PreviewListaCompleta() {
    CofrinhoDeMetasTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PreviewData.listaMetas.forEach { mct ->
                CartaoMeta(item = mct, aoClicar = {})
            }
        }
    }
}

@Preview(name = "Estado vazio (sem metas)", showBackground = true,
         widthDp = 380, heightDp = 500)
@Composable
private fun PreviewEstadoVazio() {
    CofrinhoDeMetasTheme {
        EstadoVazio(comPesquisa = false)
    }
}

@Preview(name = "Estado vazio (sem resultados de pesquisa)", showBackground = true,
         widthDp = 380, heightDp = 500)
@Composable
private fun PreviewEstadoVazioPesquisa() {
    CofrinhoDeMetasTheme {
        EstadoVazio(comPesquisa = true)
    }
}
