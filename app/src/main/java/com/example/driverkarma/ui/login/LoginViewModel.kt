package com.example.driverkarma.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverkarma.AuthRepository
import com.example.driverkarma.FakeAuthRepository
import com.example.driverkarma.data.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The view model used for the login screen.
 *
 * @param repo  The authentication repository the view model uses as the login criteria
 *
 * @author  Sifan Terefe
 */
class LoginViewModel(
    private val repo: AuthRepository = FakeAuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    /**
     * Called when the username field changes due to user input.
     *
     * @param v The new state of the string
     *
     * @author  Sifan Terefe
     */
    fun onIdentifierChange(v: String) = _state.update {
        it.copy(identifier = v, error = null)
    }

    /**
     * Called when the password field changes due to user input.
     *
     * @param v The new state of the string
     *
     * @author  Sifan Terefe
     */
    fun onPasswordChange(v: String) = _state.update {
        it.copy(password = v, error = null)
    }

    /**
     * Runs when the user attempts to sign in, with consideration to
     * the username and password fields.
     *
     * @param onSuccess The behaviour that occurs if the login is successful
     *
     * @author  Sifan Terefe
     */
    fun signIn(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.identifier.isBlank() || s.password.isBlank()) {
            _state.update {
                it.copy(error = "Enter your username or driver's license number and password.")
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, error = null)
            }
            when (repo.signIn(
                s.identifier.trim(),
                s.password.trim()
            )) {
                LoginResult.SUCCESS ->
                    onSuccess()
                LoginResult.INVALID_CREDENTIALS ->
                    _state.update {
                        it.copy(error = "Your username or password is incorrect.")
                    }
                LoginResult.NO_CONNECTION ->
                    _state.update {
                        it.copy(error = "A connection is required to sign in.")
                    }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
