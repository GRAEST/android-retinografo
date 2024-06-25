package br.com.graest.retinografo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import br.com.graest.retinografo.data.items
import br.com.graest.retinografo.data.repository.Database
import br.com.graest.retinografo.ui.screens.camera.CameraViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import br.com.graest.retinografo.ui.screens.patient.PatientDataViewModel
import br.com.graest.retinografo.ui.theme.RetinografoTheme


class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "patient.db"
        ).build()
    }

    private val patientViewModel by viewModels<PatientDataViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PatientDataViewModel(db.dao) as T
                }
            }
        }
    )


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

                val patientDataState by patientViewModel.patientDataState.collectAsState()

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

                val hideAppBarRoutes = setOf("InitialScreen", "LogInScreen", "SignUpScreenA", "SignUpScreenB")
                val navController: NavHostController = rememberNavController()
                var showAppBar by remember { mutableStateOf(true) }
                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        showAppBar = destination.route !in hideAppBarRoutes
                    }
                }


                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                fun onSelectedItemChange(index: Int): Unit {
                    selectedItemIndex = index
                }

                val scope = rememberCoroutineScope()

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


                MainScreenComposable(
                    items = items,
                    navController = navController,
                    showAppBar = showAppBar,
                    selectedItemIndex = selectedItemIndex,
                    onSelectedItemChange = ::onSelectedItemChange,
                    scope = scope,
                    drawerState = drawerState,
                    onPatientEvent = patientViewModel::onEvent
                ) {
                    RetinografoNavGraph(
                        navController,
                        controller,
                        applicationContext,
                        bitmaps,
                        bitmapSelectedIndex,
                        cameraViewModel,
                        patientViewModel,
                        patientDataState
                    )
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
