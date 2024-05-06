package br.com.graest.retinografo.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.MainScreenComposable

@Composable
fun HolderScreen(
    items: List<NavigationItem>,
    navController: NavController,
    composable: @Composable () -> Unit
) {
    MainScreenComposable(items, navController, composable)
}