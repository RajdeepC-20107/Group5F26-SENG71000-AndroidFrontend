package com.example.driverkarma.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

/* Request and response bodies, matching the FastAPI backend */
data class LoginRequest(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("profile_score") val profileScore: Int?
)

/* Every backend endpoint the app calls */
interface ApiService {
    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}