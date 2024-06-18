package br.com.graest.retinografo

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.graest.retinografo.ui.screens.InitialScreenMain
import br.com.graest.retinografo.ui.screens.camera.CameraComposableScreen
import br.com.graest.retinografo.ui.screens.camera.CameraViewModel
import br.com.graest.retinografo.ui.screens.image.ImageDetailsScreen
import br.com.graest.retinografo.ui.screens.image.VerticalGridImages
import br.com.graest.retinografo.ui.screens.login.LoginScreen
import br.com.graest.retinografo.ui.screens.login.SignUpScreen
import br.com.graest.retinografo.ui.screens.patient.PatientDataEvent
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.ui.screens.patient.PatientDataViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientScreen

@Composable
fun RetinografoNavGraph(
    navController: NavHostController = rememberNavController(),
    controller: LifecycleCameraController,
    applicationContext : Context,
    bitmaps: List<Bitmap>,
    bitmapSelectedIndex: Int,
    cameraViewModel: CameraViewModel,
    patientViewModel: PatientDataViewModel,
    patientDataState: PatientDataState,
    patientDataEvent: PatientDataEvent,

) {
    NavHost(
        navController = navController,
        startDestination = "InitialScreen"
    ) {

        composable("InitialScreen") {
            InitialScreenMain(
                onLoginClick = { navController.navigate("LogInScreen") },
                onSignUpClick = { navController.navigate("SignUpScreen") }
            )
        }

        composable("LogInScreen") {
            LoginScreen(
                onClickLogIn = { navController.navigate("Camera") }
            )
        }

        composable("SignUpScreen") {
            SignUpScreen(
                onClickSignUp = { navController.navigate("LogInScreen") }
            )
        }

        composable("Camera") {

            CameraComposableScreen(
                applicationContext = applicationContext,
                controller = controller,
                onPhotoTaken = cameraViewModel::onTakePhoto
            )

        }
        composable("Images") {

            VerticalGridImages(bitmaps)

        }
        composable("ImageDetails") {
            ImageDetailsScreen(
                bitmaps = bitmaps,
                bitmapSelectedIndex
            )
        }
        composable("Patient") {

            PatientScreen(
                state = patientDataState,
                event = patientDataEvent,
                onEvent = patientViewModel::onEvent
            )

        }
    }
}