package com.example.driverkarma.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * The app's login screen.
 *
 * @param onLoggedIn    The function that runs when a user attempts to log in
 * @param vm    The view model that the login screen uses
 *
 * @author  Sifan Terefe
 */
@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    vm: LoginViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sign In",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(24.dp))

        UsernameField(
            value = state.identifier,
            onValueChange = vm::onIdentifierChange
        )
        Spacer(Modifier.height(12.dp))

        PasswordField(
            value = state.password,
            onValueChange = vm::onPasswordChange
        )

        state.error?.let { error ->
            Spacer(Modifier.height(8.dp))
            ErrorMessage(message = error)
        }

        Spacer(Modifier.height(24.dp))

        SignInButton(
            isLoading = state.isLoading,
            onClick = { vm.signIn(onLoggedIn) }
        )
    }
}

/**
 * The username field of the login page.
 *
 * @param value The string storing the username
 * @param onValueChange Function to change the string value upon user input
 *
 * @author  Sifan Terefe
 */
@Composable
private fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Username") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * The password field of the login page.
 *
 * @param value The string storing the password
 * @param onValueChange Function to change the string value upon user input
 *
 * @author  Sifan Terefe
 */
@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * An error message that appears upon a failed login.
 *
 * @param message   The message that is displayed to the user
 *
 * @author  Sifan Terefe
 */
@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error
    )
}

/**
 * The sign-in button of the login page.
 *
 * @param isLoading Whether the login or app is loading data or not
 * @param onClick   The behaviour upon clicking the button
 *
 * @author  Sifan Terefe
 */
@Composable
private fun SignInButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text("Sign In")
        }
    }
}
