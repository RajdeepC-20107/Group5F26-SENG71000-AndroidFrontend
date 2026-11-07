package com.example.driverkarma.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverkarma.data.AuthRepository
import com.example.driverkarma.data.FakeAuthRepository
import com.example.driverkarma.data.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// LoginViewModel.kt
data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val repo: AuthRepository = FakeAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onIdentifierChange(v: String) = _state.update { it.copy(identifier = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }

    fun signIn(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.identifier.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Enter your username or driver's license number and password.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (repo.signIn(s.identifier.trim(), s.password)) {
                LoginResult.Success -> onSuccess()
                LoginResult.InvalidCredentials ->
                    _state.update { it.copy(error = "Your username or password is incorrect.") }
                LoginResult.NoConnection ->
                    _state.update { it.copy(error = "A connection is required to sign in.") }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}