package com.example.driverkarma

import android.util.Log
import com.example.driverkarma.data.LoginResult
import com.example.driverkarma.data.UserSession
import com.example.driverkarma.network.ApiClient
import com.example.driverkarma.network.ApiService
import com.example.driverkarma.network.LoginRequest
import retrofit2.HttpException
import java.io.IOException

class RemoteAuthRepository(private val api: ApiService = ApiClient.api) : AuthRepository {

    override suspend fun signIn(identifier: String, password: String): LoginResult {
        Log.d("DriverKarma", "Sending login for $identifier")
        return try {
            val user = api.login(LoginRequest(identifier, password))
            UserSession.userId = user.userId
            UserSession.fullName = user.fullName
            UserSession.profileScore = user.profileScore
            Log.d("DriverKarma", "Login worked for user ${user.userId}")
            LoginResult.SUCCESS
        } catch (e: HttpException) {
            Log.e("DriverKarma", "Server answered with code ${e.code()}", e)
            if (e.code() == 401) LoginResult.INVALID_CREDENTIALS else LoginResult.NO_CONNECTION
        } catch (e: IOException) {
            Log.e("DriverKarma", "Login request failed", e)
            LoginResult.NO_CONNECTION
        } catch (e: Exception) {
            Log.e("DriverKarma", "Unexpected error", e)
            LoginResult.NO_CONNECTION
        }
    }
}