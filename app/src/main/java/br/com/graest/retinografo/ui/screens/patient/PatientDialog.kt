package br.com.graest.retinografo.ui.screens.patient

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.IndeterminateCheckBox
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.data.model.Gender
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientDialog(
    state: PatientDataState,
    onEvent: (PatientDataEvent) -> Unit,
    onLaunchCamera: () -> Unit,
    modifier: Modifier = Modifier,
    applicationContext: Context,
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        modifier = modifier,
        title = {
            if (state.isAddingPatientData) {
                Text(text = "Add Patient")
            }
            if (state.isEditingPatientData) {
                Text(text = "Editing Patient")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)
            ) {
                if (state.isAddingPatientData || state.isEditingImage) {
                    if (state.tempImagePath != null) {
                        val bitmap = BitmapFactory.decodeFile(state.tempImagePath)
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                .aspectRatio(1f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                if (state.isEditingPatientData && !state.isEditingImage) {
                    state.profilePicture?.let { byteArrayToBitmap(it).asImageBitmap() }?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                                .aspectRatio(1f)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(PatientDataEvent.SetPatientName(it)) },
                    placeholder = { Text(text = "Name") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = state.birthDate,
                            onValueChange = { onEvent(PatientDataEvent.SetPatientBirthDate(it)) },
                            placeholder = { Text(text = "Enter date (dd-MM-yyyy)") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Right) }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        if (state.errorMessageBirthDate.isNotEmpty()) {
                            Text(
                                text = state.errorMessageBirthDate,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        DropdownMenuItem(
                            text = { Text(text=state.gender.toString()) },
                            onClick = { onEvent(PatientDataEvent.ExpandGenderMenu) },
                            leadingIcon = {
                                when (state.gender) {
                                    Gender.MALE -> { Icon(Icons.Outlined.Man, contentDescription = null)}
                                    Gender.FEMALE -> { Icon(Icons.Outlined.Woman2, contentDescription = null)}
                                    Gender.OTHER -> { Icon(Icons.Outlined.IndeterminateCheckBox, contentDescription = null) }
                                }
                            },
                            trailingIcon = {
                                Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)
                            }
                        )
                        DropdownMenu(
                            expanded = state.genderMenuExpanded,
                            onDismissRequest = {
                                onEvent(PatientDataEvent.ShrinkGenderMenu)
                            }) {
                            DropdownMenuItem(
                                text = { Text("Male") },
                                onClick = { onEvent(PatientDataEvent.SetPatientGender(Gender.MALE)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Man,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (state.gender == Gender.MALE) {
                                        Icon(Icons.Outlined.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Female") },
                                onClick = { onEvent(PatientDataEvent.SetPatientGender(Gender.FEMALE)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Woman2,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (state.gender == Gender.FEMALE) {
                                        Icon(Icons.Outlined.Check, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Other") },
                                onClick = { onEvent(PatientDataEvent.SetPatientGender(Gender.OTHER)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.IndeterminateCheckBox,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (state.gender == Gender.OTHER) {
                                        Icon(Icons.Outlined.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = state.cpf,
                    onValueChange = { onEvent(PatientDataEvent.SetPatientCPF(it)) },
                    placeholder = { Text(text = "CPF") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(PatientDataEvent.SetPatientEmail(it)) },
                    placeholder = { Text(text = "Email") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.telNumber,
                    onValueChange = { onEvent(PatientDataEvent.SetPatientTelNumber(it)) },
                    placeholder = { Text(text = "Phone Number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Is Diabetic?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Switch(
                        checked = state.isDiabetic,
                        onCheckedChange = {
                            onEvent(PatientDataEvent.SetIsDiabetic(it))
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Has HyperTension?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Switch(
                        checked = state.hasHyperTension,
                        onCheckedChange = {
                            onEvent(PatientDataEvent.SetHasHyperTension(it))
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Has Glaucoma?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Switch(
                        checked = state.hasGlaucoma,
                        onCheckedChange = {
                            onEvent(PatientDataEvent.SetHasGlaucoma(it))
                        }
                    )
                }

                OutlinedTextField(
                    value = state.description,
                    onValueChange = { onEvent(PatientDataEvent.SetDescription(it)) },
                    placeholder = { Text(text = "Description") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        onLaunchCamera()
                        onEvent(PatientDataEvent.ClickEditImage)
                    }) {
                        if (state.isAddingPatientData) {
                            Text(text = "Add photo")
                        }
                        if (state.isEditingPatientData) {
                            Text(text = "Edit photo")
                        }
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    if (state.tempImagePath != null) {
                        Text(text = "Image Added")
                    } else {
                        Text(text = "No image yet.")
                    }
                }
            }
        },
        onDismissRequest = {
            onEvent(PatientDataEvent.HideDialog)
        },
        confirmButton = {
            if (state.isAddingPatientData) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        onEvent(PatientDataEvent.SavePatientData(applicationContext))
                    }) {
                        Text(text = "Save")
                    }
                }
            }
            if (state.isEditingPatientData) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onEvent(PatientDataEvent.HideDialog)
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Text(text = "Cancel", color = MaterialTheme.colorScheme.onError)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            onEvent(PatientDataEvent.SavePatientData(applicationContext))
                        }) {
                            Text(text = "Save")
                        }
                    }
                    IconButton(onClick = {
                        state.patientId?.let { PatientDataEvent.DeletePatientDataById(it) }
                            ?.let { onEvent(it) }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Patient"
                        )
                    }
                }
            }
        })
}