package pt.ipbeja.cofrinho.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipbeja.cofrinho.data.MetaRepository
import pt.ipbeja.cofrinho.ui.ViewModelFactory
import pt.ipbeja.cofrinho.ui.detalhe.DetalheMetaScreen
import pt.ipbeja.cofrinho.ui.detalhe.DetalheMetaViewModel
import pt.ipbeja.cofrinho.ui.editar.EditarMetaScreen
import pt.ipbeja.cofrinho.ui.editar.EditarMetaViewModel
import pt.ipbeja.cofrinho.ui.metas.MetasListScreen
import pt.ipbeja.cofrinho.ui.metas.MetasListViewModel

object Rotas {
    const val LISTA = "lista"
    const val DETALHE = "detalhe/{metaId}"
    const val EDITAR = "editar/{metaId}"
    fun detalhe(id: Long) = "detalhe/$id"
    fun editar(id: Long) = "editar/$id"
}

@Composable
fun CofrinhoNavHost(repository: MetaRepository) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rotas.LISTA) {

        composable(Rotas.LISTA) {
            val vm: MetasListViewModel = viewModel(factory = ViewModelFactory(repository))
            MetasListScreen(
                vm = vm,
                aoAbrirMeta = { id -> nav.navigate(Rotas.detalhe(id)) },
                aoCriar = { nav.navigate(Rotas.editar(0L)) }
            )
        }

        composable(
            route = Rotas.DETALHE,
            arguments = listOf(navArgument("metaId") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("metaId") ?: 0L
            val vm: DetalheMetaViewModel = viewModel(
                factory = ViewModelFactory(repository, id),
                key = "detalhe-$id"
            )
            DetalheMetaScreen(
                vm = vm,
                aoVoltar = { nav.popBackStack() },
                aoEditar = { nav.navigate(Rotas.editar(id)) }
            )
        }

        composable(
            route = Rotas.EDITAR,
            arguments = listOf(navArgument("metaId") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("metaId") ?: 0L
            val vm: EditarMetaViewModel = viewModel(
                factory = ViewModelFactory(repository, id),
                key = "editar-$id"
            )
            EditarMetaScreen(
                vm = vm,
                aoVoltar = { nav.popBackStack() },
                aoGuardado = { _ -> nav.popBackStack() },
                aoEliminado = {
                    // Ao eliminar, sair para a lista (fecha Editar e Detalhe se veio de lá)
                    nav.popBackStack(Rotas.LISTA, inclusive = false)
                }
            )
        }
    }
}
