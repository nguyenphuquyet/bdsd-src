package com.quyetdz.noti

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.quyetdz.noti.model.ClearTokenRequest
import com.quyetdz.noti.network.ApiClient
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        session = SessionManager(this)

        // Nếu chưa đăng nhập thì bắn về màn login
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        findViewById<TextView>(R.id.tvFullName).text =
            session.getFullName()?.takeIf { it.isNotBlank() } ?: session.getUsername()
        findViewById<TextView>(R.id.tvUsername).text = "@" + session.getUsername()
        findViewById<TextView>(R.id.tvEmail).text =
            session.getEmail()?.takeIf { it.isNotBlank() } ?: "Chưa cập nhật"

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val userId = session.getUserId()
            btnLogoutSetEnabled(false)

            lifecycleScope.launch {
                try {
                    ApiClient.api.clearToken(ClearTokenRequest(userId))
                } catch (_: Exception) {
                } finally {
                    session.logout()
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun btnLogoutSetEnabled(enabled: Boolean) {
        findViewById<Button>(R.id.btnLogout).isEnabled = enabled
    }
}