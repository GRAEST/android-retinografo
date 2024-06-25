package br.com.graest.retinografo.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun EditUserData() {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        Text(
            text = "My data",
            fontSize = 26.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
            Row {
                IconButton(
                    onClick = {
                        //choose between camera and photos
                    },
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {
                    //onEvent(PatientDataEvent.SetPatientName(it))
                },
                placeholder = {
                    Text(text = "Name")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        //onEvent(PatientDataEvent.SetPatientAge(it))
                    },
                    placeholder = {
                        Text(text = "Age")
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.padding(4.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {
                        //onEvent(PatientDataEvent.SetPatientAge(it))
                    },
                    placeholder = {
                        Text(text = "Age")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = "",
                onValueChange = {
                    //onEvent(PatientDataEvent.SetPatientName(it))
                },
                placeholder = {
                    Text(text = "Main Email")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "",
                onValueChange = {
                    //onEvent(PatientDataEvent.SetPatientName(it))
                },
                placeholder = {
                    Text(text = "Secondary Email")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "",
                onValueChange = {
                    //onEvent(PatientDataEvent.SetPatientName(it))
                },
                placeholder = {
                    Text(text = "CRM")
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "",
                onValueChange = {
                    //onEvent(PatientDataEvent.SetPatientName(it))
                },
                placeholder = {
                    Text(text = "Phone Number")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }


        Spacer(modifier = Modifier.padding(10.dp))

        Button(onClick = {
            //go to edit patient
        }) {
            Text(
                text = "Save",
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditUserData() {
    EditUserData()
}