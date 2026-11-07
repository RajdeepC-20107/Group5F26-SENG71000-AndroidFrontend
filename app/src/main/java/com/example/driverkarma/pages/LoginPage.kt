package com.example.driverkarma.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "DriverKarma",
                fontSize = 48.sp,
                modifier = Modifier.padding(0.dp, 32.dp)
            )
        }
        Row {
            UsernameField(navController)
        }
        Row {
            PasswordField(navController)
        }
        Row {
            LoginButton(navController)
        }
    }
}

@Composable
private fun UsernameField(navController: NavController) {
    val usernameState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        state = usernameState,
        label = { Text("Username or E-mail") },
        placeholder = { Text("Enter your username") },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        onKeyboardAction = {
            keyboardController?.hide()
            login(navController)
        },
        modifier = Modifier.padding(32.dp, 0.dp).fillMaxWidth()
    )
}

@Composable
private fun PasswordField(navController: NavController) {
    val passwordState = rememberTextFieldState()
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    SecureTextField(
        state = passwordState,
        label = { Text("Password") },
        textObfuscationMode =
            if(passwordVisible) TextObfuscationMode.Visible
            else TextObfuscationMode.Hidden,
        placeholder = { Text("Enter Password") },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = {
            keyboardController?.hide()
            login(navController)
        },
        trailingIcon = {
            val image =
                if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisible) "Hide password"
                else "Show password"
            IconButton(
                onClick = {
                    passwordVisible = !passwordVisible
                }
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = description
                )
            }
        },
        modifier = Modifier
            .padding(32.dp, 16.dp)
            .fillMaxWidth(),

    )
}

@Composable
private fun LoginButton(navController: NavController) {
    Button(
        onClick = { login(navController) }
    ) {
        Text(
            text = "Log In"
        )
    }
}

private fun login(navController: NavController) {
    navController.navigate("home")
}