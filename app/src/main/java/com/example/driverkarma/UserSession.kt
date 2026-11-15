package com.example.driverkarma.data

/* The signed-in driver, filled in after a successful login */
object UserSession {
    var userId: Int? = null
    var fullName: String? = null
    var profileScore: Int? = null

    fun clear() {
        userId = null
        fullName = null
        profileScore = null
    }
}