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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.graest.retinografo.R

@Composable
fun UserData(
    onClickEdit: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier.padding(10.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Eduardo Ribeiro",
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
            Text(
                text = "61 anos",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "eduardo@gmail.com",
                fontSize = 20.sp
            )
            Text(
                text = "+55 (11) 9 9999-9999",
                fontSize = 20.sp
            )
            Text(
                text = "CRM-SP 12345",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))

        Button(onClick = { onClickEdit() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp)
            ){
                Text(
                    text = "Edit",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit User Data"
                )
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
fun PreviewUserData(){
    UserData(onClickEdit = {})
}