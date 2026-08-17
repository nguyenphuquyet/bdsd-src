package com.quyetdz.noti.model

data class LoginRequest(
    val username: String,
    val password: String,
    val fcm_token: String
)

data class SaveTokenRequest(
    val user_id: Int,
    val fcm_token: String
)

data class ClearTokenRequest(
    val user_id: Int
)

data class User(
    val id: Int,
    val username: String,
    val full_name: String?,
    val email: String?,
    val is_admin: Boolean
)

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val user: User? = null
)

data class SimpleResponse(
    val success: Boolean,
    val message: String? = null
)