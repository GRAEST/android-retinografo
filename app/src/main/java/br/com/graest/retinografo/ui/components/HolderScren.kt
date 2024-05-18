package br.com.graest.retinografo.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.MainScreenComposable
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import kotlinx.coroutines.CoroutineScope

@Composable
fun HolderScreen(
    items: List<NavigationItem>,
    navController: NavController,
    selectedItemIndex: Int,
    onSelectedItemChange: (Int) -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onPatientEvent: (PatientDataEvent) -> Unit,
    composable: @Composable () -> Unit

) {
    MainScreenComposable(
        items,
        navController,
        selectedItemIndex,
        onSelectedItemChange,
        scope,
        drawerState,
        onPatientEvent,
        composable
    )
}