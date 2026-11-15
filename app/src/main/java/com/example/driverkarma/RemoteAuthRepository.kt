package com.example.driverkarma

import com.example.driverkarma.data.LoginResult
import com.example.driverkarma.data.UserSession
import com.example.driverkarma.network.ApiClient
import com.example.driverkarma.network.ApiService
import com.example.driverkarma.network.LoginRequest
import retrofit2.HttpException
import java.io.IOException

/* Logs in through the FastAPI backend's POST /login */
class RemoteAuthRepository(
    private val api: ApiService = ApiClient.api
) : AuthRepository {

    override suspend fun signIn(identifier: String, password: String): LoginResult {
        return try {
            val user = api.login(LoginRequest(identifier, password))
            UserSession.userId = user.userId
            UserSession.fullName = user.fullName
            UserSession.profileScore = user.profileScore
            LoginResult.SUCCESS
        } catch (e: HttpException) {
            // 401 means a wrong username, licence number or password
            if (e.code() == 401) LoginResult.INVALID_CREDENTIALS
            else LoginResult.NO_CONNECTION
        } catch (e: IOException) {
            // No internet, or the backend isn't running
            LoginResult.NO_CONNECTION
        }
    }
}