package br.com.graest.retinografo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.CameraEnhance
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val items = listOf(
    NavigationItem(
        title = "Patient",
        selectedIcon = Icons.Filled.VerifiedUser,
        unselectedIcon = Icons.Outlined.VerifiedUser,
        route = "Patient"
    ),
    NavigationItem(
        title = "Exam Camera",
        selectedIcon = Icons.Filled.CameraEnhance,
        unselectedIcon = Icons.Outlined.CameraEnhance,
        route = "ExamCamera"
    ),
    NavigationItem(
        title = "Exams",
        selectedIcon = Icons.Filled.MedicalServices,
        unselectedIcon = Icons.Outlined.MedicalServices,
        route = "Exams"
    )
)