package br.com.graest.retinografo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import br.com.graest.retinografo.ui.screens.exam.ExamDataViewModel
import br.com.graest.retinografo.ui.screens.login.LoginViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientDataViewModel
import br.com.graest.retinografo.ui.screens.signup.SignUpViewModel
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
                    return PatientDataViewModel(db.patientDataDao) as T
                }
            }
        }
    )
    private val examViewModel by viewModels<ExamDataViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExamDataViewModel(db.examDataDao) as T
                }
            }
        }
    )


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

                val signUpViewModel: SignUpViewModel = viewModel()
                val signUpState by signUpViewModel.signUpState.collectAsState()

                val loginViewModel: LoginViewModel = viewModel()
                val loginState by loginViewModel.loginState.collectAsState()


                val examDataState by examViewModel.examDataState.collectAsState()

                val patientDataState by patientViewModel.patientDataState.collectAsState()

                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }

                val hideAppBarRoutes = setOf("InitialScreen", "LogInScreen", "SignUpScreenA", "SignUpScreenB")

                val navController: NavHostController = rememberNavController()

                var showAppBar by remember { mutableStateOf(true) }

                LaunchedEffect(navController, examDataState.patientSelected) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        showAppBar = destination.route !in hideAppBarRoutes && !(examDataState.patientSelected)
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
                    onImageClick = {navController.navigate("UserData")},
                    drawerState = drawerState,
                    onPatientEvent = patientViewModel::onEvent
                ) {
                    RetinografoNavGraph(
                        navController,
                        controller,
                        applicationContext,
                        patientViewModel,
                        patientDataState,
                        examViewModel,
                        examDataState,
                        signUpViewModel,
                        signUpState,
                        loginViewModel,
                        loginState
                    )
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }

}
