package br.com.graest.retinografo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PresentToAll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.graest.retinografo.R
import br.com.graest.retinografo.data.remote.RequestSender
import br.com.graest.retinografo.ui.getCurrentRoute
import br.com.graest.retinografo.ui.screens.exam.ExamDataEvent
import br.com.graest.retinografo.ui.screens.exam.ExamDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComposable(
    navController: NavController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onImageClick: () -> Unit,
    onEvent: (ExamDataEvent) -> Unit,
    examDataState: ExamDataState,
    requestSender : RequestSender
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 20.dp
                    )

            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.weight(1f)
                ) {
                    when(getCurrentRoute(navController = navController)){
                        "PatientCamera"-> {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back"
                                )
                            }
                        }
                        "UserData"-> {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back"
                                )
                            }
                        }
                        "EditUserData"-> {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back"
                                )
                            }
                        }
                        "ExamDetails"-> {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Arrow Back"
                                )
                            }
                        }
                        else -> {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                when(getCurrentRoute(navController = navController))  {
                     "PatientCamera" -> {
                        Text(
                            text = "Edit Patient Photo",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(4f)
                        )
                    }
                    "UserData" -> {
                        Text(
                            text = "My Data",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(4f)
                        )
                    }
                    "EditUserData" -> {
                        Text(
                            text = "Edit Data",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(4f)
                        )
                    }
                    else -> {
                        Text(
                            text = "Retinografo Digital",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(4f)
                        )
                    }
                }


                Spacer(modifier = Modifier.weight(1f))

                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.weight(1f)
                ) {
                    when(getCurrentRoute(navController = navController)) {
                        "PatientCamera" -> {  }
                        "UserData" -> {  }
                        "EditUserData" -> {  }
                        "ExamDetails" -> {
                            IconButton(onClick = {
                                if (examDataState.patientData != null && examDataState.examData != null) {
                                    onEvent(ExamDataEvent.OnSendToCloud(
                                        requestSender = requestSender,
                                        examDataId = examDataState.examData.id,
                                        patientData = examDataState.patientData
                                    ))
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.PresentToAll,
                                    contentDescription = "Send Images to Cloud"
                                )
                            }
                        }
                        else -> {
                            IconButton(onClick = {
                                onImageClick()
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "User Photo",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                )
                            }
                        }
                    }

                }
            }
        },
    )
}