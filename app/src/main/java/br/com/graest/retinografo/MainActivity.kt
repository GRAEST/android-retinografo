package br.com.graest.retinografo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.graest.retinografo.data.NavigationItem
import br.com.graest.retinografo.model.CameraViewModel
import br.com.graest.retinografo.ui.screens.CameraComposableScreen
import br.com.graest.retinografo.ui.screens.HolderScreen
import br.com.graest.retinografo.ui.screens.VerticalGridImages
import br.com.graest.retinografo.ui.theme.RetinografoTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }



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

                val scope = rememberCoroutineScope()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }
                val viewModel = viewModel<CameraViewModel>()
                val bitmaps by viewModel.bitmaps.collectAsState()

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "Camera") {

                    composable("Camera") {
                        HolderScreen(items, navController) {
                            CameraComposableScreen(
                                applicationContext = applicationContext,
                                controller = controller,
                                scope = scope,
                                onPhotoTaken = viewModel::onTakePhoto
                            )
                        }
                    }
                    composable("Images") {
                        HolderScreen(items, navController) {
                            VerticalGridImages(bitmaps)
                        }
                    }
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }


}
