package br.com.graest.retinografo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.ui.components.DrawerContentExpanded
import br.com.graest.retinografo.ui.components.topBarComposable
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreenComposable(
    items: List<NavigationItem>,
    navController: NavController,
    selectedItemIndex: Int,
    onSelectedItemChange: (Int) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onPatientEvent: (PatientDataEvent) -> Unit,
    composable: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                DrawerContentExpanded(items, selectedItemIndex, onSelectedItemChange, scope, drawerState, navController)
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    topBarComposable(scope, drawerState)
                },
                floatingActionButton = {
                    if (selectedItemIndex == 2) {
                        FloatingActionButton(onClick = {
                            onPatientEvent(PatientDataEvent.ShowDialog)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Contact"
                            )
                        }
                    }
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