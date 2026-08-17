package com.quyetdz.noti

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("push_app_session", Context.MODE_PRIVATE)

    fun saveUser(id: Int, username: String, fullName: String?, email: String?) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putInt("user_id", id)
            .putString("username", username)
            .putString("full_name", fullName)
            .putString("email", email)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getUsername(): String? = prefs.getString("username", null)
    fun getFullName(): String? = prefs.getString("full_name", null)
    fun getEmail(): String? = prefs.getString("email", null)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
