package com.example.driverkarma.network

import com.example.driverkarma.network.login.LoginRequest
import com.example.driverkarma.network.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Every FastAPI backend endpoint the app calls.
 *
 * @author  Farah Lariani
 */
interface ApiService {

    /**
     * Checks the user's login details against the backend.
     *
     * @param body  The identifier and password the user typed
     * @return      The signed-in user's info, or throws an HttpException 401 if the details are wrong
     */
    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}