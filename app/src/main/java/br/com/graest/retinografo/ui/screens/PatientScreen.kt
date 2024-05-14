package br.com.graest.retinografo.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.data.PatientData

@Composable
fun PatientScreen(
    patientList: List<PatientData>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(patientList) { patientData ->
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row {
                    Text(
                        text = patientData.name,
                        fontSize = 24.sp,
                        modifier = Modifier.weight(3F)
                            .wrapContentWidth(Alignment.Start)
                    )
                    Text(
                        text = "${patientData.age.toString()} anos",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.weight(1F)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .align(Alignment.CenterVertically)

                    )
                }
                Text(
                    text = patientData.potential,
                    fontSize = 16.sp,
                    modifier = Modifier.wrapContentWidth(Alignment.Start)
                )
            }

        }
    }
}

val mockdata = listOf(
    PatientData(
        name = "João Alberto",
        age = 22,
        potential = "Diabético, Hipertenso"
    ),
    PatientData(
        name = "Carlos Alberto",
        age = 72,
        potential = "Anemia, Idoso"
    ),
    PatientData(
        name = "Mario Alberto",
        age = 42,
        potential = "Saudável"
    )
)

@Preview(showBackground = true)
@Composable
fun PatientScreenPreview() {
    PatientScreen(patientList = mockdata)
}
