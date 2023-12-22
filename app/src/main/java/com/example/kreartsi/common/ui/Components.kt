package com.example.kreartsi.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreartsi.common.theme.Black
import com.example.kreartsi.common.theme.Black24
import com.example.kreartsi.common.theme.Gray
import com.example.kreartsi.common.theme.White

@Composable
fun ImageDrawable(
    res: Int,
    modifier: Modifier = Modifier,
    description: String = "",
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = res),
        contentDescription = description,
        contentScale = contentScale)
}

@Composable
fun IconVector(
    modifier: Modifier = Modifier,
    description: String = "",
    tint: Color = White,
    vector: ImageVector) {
    Icon(
        modifier = modifier,
        imageVector = vector,
        contentDescription = description,
        tint = tint)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation,

) {
    Column {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = label,
            color = White,
            fontSize = 15.sp,
            fontWeight = FontWeight.W700)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = label, color = Black24)
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            shape = RoundedCornerShape(10.dp),
            visualTransformation = visualTransformation,
            trailingIcon = null,
        )
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String = "Label",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gray
        )) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Black)
    }
}

@Preview
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String = "Label",
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black
        )) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = White)
    }
}