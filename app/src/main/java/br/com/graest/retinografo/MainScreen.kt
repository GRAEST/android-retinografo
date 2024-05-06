package br.com.graest.retinografo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.ui.components.DrawerContentExpanded
import br.com.graest.retinografo.ui.components.topBarComposable

@Composable
fun MainScreenComposable(
    items: List<NavigationItem>,
    navController: NavController,
    composable: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                DrawerContentExpanded(items, selectedItemIndex, scope, drawerState, navController)
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    topBarComposable(scope, drawerState)
                }
            ) { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    composable()
                }
            }
        }
    }
}