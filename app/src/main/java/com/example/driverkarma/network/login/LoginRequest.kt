package com.example.driverkarma.network.login

/**
 * Request body sent to POST /login on the FastAPI backend.
 *
 * @param identifier    The username or driver's license number the user typed
 * @param password      The password the user typed
 *
 * @author  Farah Lariani
 */
data class LoginRequest(
    val identifier: String,
    val password: String
)