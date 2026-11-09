package com.example.driverkarma

import com.example.driverkarma.data.LoginResult

interface AuthRepository {
    suspend fun signIn(identifier: String, password: String): LoginResult
}

class FakeAuthRepository : AuthRepository {
    override suspend fun signIn(identifier: String, password: String): LoginResult {
        /* any username and password combination
        should work, just a stub*/
        return if (identifier.isNotBlank() && password.isNotBlank()) {
            LoginResult.SUCCESS
        } else {
            LoginResult.INVALID_CREDENTIALS
        }
    }
}
