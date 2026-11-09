package com.example.driverkarma.ui.login

data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
