package com.quyetdz.noti.network

import com.quyetdz.noti.model.LoginRequest
import com.quyetdz.noti.model.LoginResponse
import com.quyetdz.noti.model.SaveTokenRequest
import com.quyetdz.noti.model.ClearTokenRequest
import com.quyetdz.noti.model.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/save_token.php")
    suspend fun saveToken(@Body request: SaveTokenRequest): Response<SimpleResponse>

    // Gọi khi logout để gỡ token FCM khỏi tài khoản trên server
    @POST("api/clear_token.php")
    suspend fun clearToken(@Body request: ClearTokenRequest): Response<SimpleResponse>
}