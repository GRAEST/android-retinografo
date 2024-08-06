package br.com.graest.retinografo.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import br.com.graest.retinografo.ui.screens.exam.ExamDetailsScreen
import br.com.graest.retinografo.ui.screens.exam.ExamList
import br.com.graest.retinografo.ui.screens.login.LoginScreen
import br.com.graest.retinografo.ui.screens.login.LoginState
import br.com.graest.retinografo.ui.screens.login.LoginViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientCameraComposable
import br.com.graest.retinografo.ui.screens.patient.PatientDataState
import br.com.graest.retinografo.ui.screens.patient.PatientDataViewModel
import br.com.graest.retinografo.ui.screens.patient.PatientScreen
import br.com.graest.retinografo.ui.screens.signup.SignUpCameraComposable
import br.com.graest.retinografo.ui.screens.signup.SignUpScreenA
import br.com.graest.retinografo.ui.screens.signup.SignUpScreenB
import br.com.graest.retinografo.ui.screens.signup.SignUpState
import br.com.graest.retinografo.ui.screens.signup.SignUpViewModel
import br.com.graest.retinografo.ui.screens.user.EditUserData
import br.com.graest.retinografo.ui.screens.user.UserData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RetinografoNavGraph(
    navController: NavHostController = rememberNavController(),
    controller: LifecycleCameraController,
    applicationContext: Context,
    patientViewModel: PatientDataViewModel,
    patientDataState: PatientDataState,
    examDataViewModel: ExamDataViewModel,
    examDataState: ExamDataState,
    signUpViewModel: SignUpViewModel,
    signUpState: SignUpState,
    loginViewModel: LoginViewModel,
    loginState: LoginState,
    flashViewModel: FlashViewModel,
    flashState: FlashState
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
                loginViewModel = loginViewModel,
                loginState = loginState,
                onEvent = loginViewModel::onEvent,
                onClickLogIn = { navController.navigate("Patient") }
            )
        }

        composable("SignUpScreenA") {
            SignUpScreenA(
                signUpState = signUpState,
                onEvent = signUpViewModel::onEvent,
                onClickNext = { navController.navigate("SignUpScreenB") },
                onLaunchCamera = { navController.navigate("UserCamera") }
            )
        }

        composable("UserCamera") {
            SignUpCameraComposable(
                applicationContext = applicationContext,
                controller = controller,
                navController = navController,
                viewModel = signUpViewModel,
                onEvent = signUpViewModel::onEvent
            )
        }

        composable("SignUpScreenB") {
            SignUpScreenB(
                viewModel = signUpViewModel,
                signUpState = signUpState,
                onEvent = signUpViewModel::onEvent,
                onClickSignUp = { navController.navigate("LogInScreen") }
            )
        }

        composable("ExamCamera") {
            ExamCameraComposableScreen(
                patientDataState = patientDataState,
                examDataState = examDataState,
                examDataViewModel = examDataViewModel,
                onEvent = examDataViewModel::onEvent,
                applicationContext = applicationContext,
                controller = controller,
                navController = navController,
                flashViewModel = flashViewModel,
                flashState = flashState,
                onFlashEvent = flashViewModel::onEvent,
                onNavigateToDetails = {  navController.navigate("ExamDetails") }
            )

        }

        composable("Exams") {
            ExamList(
                examDataState,
                examDataViewModel::onEvent
            ) { navController.navigate("ExamDetails") }
        }

        composable("ExamDetails") {
            ExamDetailsScreen(
                state = examDataState,
                onEvent = examDataViewModel::onEvent
            )
        }

        composable("Patient") {

            PatientScreen(
                state = patientDataState,
                onEvent = patientViewModel::onEvent,
                onLaunchCamera = { navController.navigate("PatientCamera") },
                applicationContext = applicationContext
            )
        }
        composable("PatientCamera") {
            PatientCameraComposable(
                applicationContext = applicationContext,
                controller = controller,
                navController = navController,
                viewModel = patientViewModel,
                onEvent = patientViewModel::onEvent
            )
        }
        composable("UserData") {

            UserData(onClickEdit = { navController.navigate("EditUserData") })

        }
        composable("EditUserData") {
            EditUserData()
        }


    }
}

@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}