package com.example.driverkarma.data

interface AuthRepository {
    suspend fun signIn(identifier: String, password: String): LoginResult
}

class FakeAuthRepository : AuthRepository {
    override suspend fun signIn(identifier: String, password: String): LoginResult {
        /* any username and password combination
        should work, just a stub*/
        return if (identifier.isNotBlank() && password.isNotBlank()) {
            LoginResult.Success
        } else {
            LoginResult.InvalidCredentials
        }
    }
}
