package br.com.graest.retinografo

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.graest.retinografo.ui.screens.InitialScreenMain
import br.com.graest.retinografo.ui.screens.exam.ExamCameraComposableScreen
import br.com.graest.retinografo.ui.screens.exam.ExamDataState
import br.com.graest.retinografo.ui.screens.exam.ExamDataViewModel
import br.com.graest.retinografo.ui.screens.exam.ImageDetailsScreen
import br.com.graest.retinografo.ui.screens.exam.VerticalGridImages
import br.com.graest.retinografo.ui.screens.login.LoginScreen
import br.com.graest.retinografo.ui.screens.patient.PatientCameraComposable
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.ui.screens.patient.PatientDataViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientScreen
import br.com.graest.retinografo.ui.screens.signup.SignUpScreenA
import br.com.graest.retinografo.ui.screens.signup.SignUpScreenB
import br.com.graest.retinografo.ui.screens.user.EditUserData
import br.com.graest.retinografo.ui.screens.user.UserData

@Composable
fun RetinografoNavGraph(
    navController: NavHostController = rememberNavController(),
    controller: LifecycleCameraController,
    applicationContext : Context,
    bitmaps: List<Bitmap>,
    bitmapSelectedIndex: Int,
    patientViewModel: PatientDataViewModel,
    patientDataState: PatientDataState,
    examDataViewModel: ExamDataViewModel,
    examDataState: ExamDataState,

) {
    NavHost(
        navController = navController,
        startDestination = "InitialScreen"
    ) {

        composable("InitialScreen") {
            InitialScreenMain(
                onLoginClick = { navController.navigate("LogInScreen") },
                onSignUpClick = { navController.navigate("SignUpScreenA") }
            )
        }

        composable("LogInScreen") {
            LoginScreen(
                onClickLogIn = { navController.navigate("Camera") }
            )
        }

        composable("SignUpScreenA") {
            SignUpScreenA(
                onClickSignUp = { navController.navigate("SignUpScreenB") }
            )
        }

        composable("SignUpScreenB") {
            SignUpScreenB(
                onClickSignUp = { navController.navigate("LogInScreen") }
            )
        }

        composable("Camera") {

            ExamCameraComposableScreen(
                patientDataState = patientDataState,
                examDataState = examDataState,
                examDataViewModel = examDataViewModel,
                onEvent = examDataViewModel::onEvent,
                applicationContext = applicationContext,
                controller = controller,
                onPhotoTaken = examDataViewModel::onTakePhoto
            )

        }
        composable("Images") {

            VerticalGridImages(examDataState)

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
                onEvent = patientViewModel::onEvent,
                onLaunchCamera = { navController.navigate("PatientCamera") },
                viewModel = patientViewModel
            )
        }
        composable("PatientCamera") {
            PatientCameraComposable(
                applicationContext = applicationContext,
                controller = controller,
                navController = navController,
                viewModel = patientViewModel
            )
        }
        composable("UserData") {

            UserData(onClickEdit = { navController.navigate("EditUserData") })

        }
        composable("EditUserData"){
            EditUserData()
        }



    }
}

@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}