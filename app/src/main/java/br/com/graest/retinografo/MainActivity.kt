package br.com.graest.retinografo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.ui.screens.HolderScreen
import br.com.graest.retinografo.ui.theme.RetinografoTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetinografoTheme {
                val items = listOf(
                    NavigationItem(
                        title = "Camera",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        route = "Camera"
                    ),
                    NavigationItem(
                        title = "Images",
                        selectedIcon = Icons.Filled.Info,
                        unselectedIcon = Icons.Outlined.Info,
                        route = "Images"
                    )
                )

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Camera") {

                    composable("Camera") {
                        HolderScreen(items, navController) {
                            Text(text = "Camera")
                        }
                    }
                    composable("Images") {
                        HolderScreen(items, navController) {
                            Text(text = "Images")
                        }
                    }
                }
            }
        }
    }


}
