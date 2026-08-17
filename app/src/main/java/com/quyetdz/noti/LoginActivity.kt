package com.quyetdz.noti

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.quyetdz.noti.model.LoginRequest
import com.quyetdz.noti.network.ApiClient
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var session: SessionManager

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(
                this,
                "Bạn cần cấp quyền thông báo để nhận được push từ hệ thống",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        askNotificationPermission()

        session = SessionManager(this)

        if (session.isLoggedIn()) {
            goToProfile()
            return
        }

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        btnLogin.setOnClickListener { attemptLogin() }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun attemptLogin() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        setLoading(true)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val fcmToken = if (task.isSuccessful) task.result ?: "" else ""
            doLogin(username, password, fcmToken)
        }
    }

    private fun doLogin(username: String, password: String, fcmToken: String) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.api.login(LoginRequest(username, password, fcmToken))
                setLoading(false)

                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()!!.user!!
                    session.saveUser(user.id, user.username, user.full_name, user.email)
                    Toast.makeText(this@LoginActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    goToProfile()
                } else {
                    val msg = response.body()?.message ?: "Đăng nhập thất bại"
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                setLoading(false)
                Toast.makeText(this@LoginActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        btnLogin.isEnabled = !loading
    }

    private fun goToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }
}
