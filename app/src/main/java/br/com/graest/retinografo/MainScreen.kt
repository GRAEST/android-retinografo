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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.ui.components.DrawerContentExpanded
import br.com.graest.retinografo.ui.components.TopBarComposable
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreenComposable(
    items: List<NavigationItem>,
    navController: NavController,
    showAppBar: Boolean,
    selectedItemIndex: Int,
    onSelectedItemChange: (Int) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onImageClick: () -> Unit,
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
                    if ( showAppBar ) {
                        TopBarComposable(navController, scope, drawerState, onImageClick)
                    }
                },
                floatingActionButton = {
                    if (getCurrentRoute(navController = navController) == "Patient") {
                        FloatingActionButton(onClick = {
                            onPatientEvent(PatientDataEvent.ShowAddPatientDialog)
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
