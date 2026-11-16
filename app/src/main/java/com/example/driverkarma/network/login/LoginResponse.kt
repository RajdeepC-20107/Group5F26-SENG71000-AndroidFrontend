package com.example.driverkarma.network.login

import com.google.gson.annotations.SerializedName

/**
 * Response body returned by POST /login on the FastAPI backend.
 *
 * @param userId        The signed-in user's ID
 * @param fullName      The signed-in user's full name
 * @param profileScore  The user's overall driving score, or null if they have none yet
 *
 * @author  Farah Lariani
 */
data class LoginResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("profile_score") val profileScore: Int?
)