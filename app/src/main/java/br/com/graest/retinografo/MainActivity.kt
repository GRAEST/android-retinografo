package br.com.graest.retinografo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.graest.retinografo.data.items
import br.com.graest.retinografo.model.CameraViewModel
import br.com.graest.retinografo.ui.components.HolderScreen
import br.com.graest.retinografo.ui.screens.CameraComposableScreen
import br.com.graest.retinografo.ui.screens.ImageDetailsScreen
import br.com.graest.retinografo.ui.screens.InitialScreenMain
import br.com.graest.retinografo.ui.screens.LoginScreen
import br.com.graest.retinografo.ui.screens.PatientScreen
import br.com.graest.retinografo.ui.screens.SignUpScreen
import br.com.graest.retinografo.ui.screens.VerticalGridImages
import br.com.graest.retinografo.ui.screens.mockdata
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

                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }

                val cameraViewModel = viewModel<CameraViewModel>()
                val bitmaps by cameraViewModel.bitmaps.collectAsState()
                val bitmapSelectedIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                val navController : NavHostController = rememberNavController()

                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                fun onSelectedItemChange(index: Int): Unit {
                    selectedItemIndex = index
                }

                val scope = rememberCoroutineScope()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


                NavHost(navController = navController, startDestination = "InitialScreen") {


                    composable("InitialScreen") {
                        InitialScreenMain(
                            onLoginClick = { navController.navigate("LogInScreen") },
                            onSignUpClick = { navController.navigate("SignUpScreen") }
                        )
                    }

                    composable("LogInScreen") {
                        LoginScreen(
                            onClickLogIn = { navController.navigate("Camera")}
                        )
                    }

                    composable("SignUpScreen") {
                        SignUpScreen(
                            onClickSignUp = { navController.navigate("LogInScreen")}
                        )
                    }

                    composable("Camera") {
                        HolderScreen(
                            items,
                            navController,
                            selectedItemIndex,
                            ::onSelectedItemChange,
                            scope,
                            drawerState
                        ) {
                            CameraComposableScreen(
                                applicationContext = applicationContext,
                                controller = controller,
                                onPhotoTaken = cameraViewModel::onTakePhoto
                            )
                        }
                    }
                    composable("Images") {
                        HolderScreen(
                            items,
                            navController,
                            selectedItemIndex,
                            ::onSelectedItemChange,
                            scope,
                            drawerState
                        ) {
                            VerticalGridImages(bitmaps)
                        }
                    }
                    composable("ImageDetails") {
                        ImageDetailsScreen(
                            bitmaps = bitmaps,
                            bitmapSelectedIndex
                        )
                    }
                    composable("Patient") {
                        HolderScreen(
                            items = items,
                            navController = navController,
                            selectedItemIndex = selectedItemIndex,
                            onSelectedItemChange = ::onSelectedItemChange,
                            scope = scope,
                            drawerState = drawerState
                        ) {
                            PatientScreen(patientList = mockdata)
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
