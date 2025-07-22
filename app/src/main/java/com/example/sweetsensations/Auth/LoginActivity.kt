package com.example.sweetsensations.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.Admin.AdminActivity
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: DatabaseReference by lazy { FirebaseDatabase.getInstance().getReference("users") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek session saat activity pertama kali dibuka
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // Jika sudah login, arahkan ke halaman berdasarkan role
            val currentUserId = sharedPreferences.getString("user_id", null)
            currentUserId?.let {
                checkUserRole(it)
            }
        } else {
            // Jika belum login, tampilkan form login
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnLogin.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUserId = auth.currentUser?.uid
                            currentUserId?.let {
                                saveSession(it)  // Simpan session setelah login berhasil
                                writeToActivityHistory(email)  // Simpan riwayat aktivitas
                                checkUserRole(it)  // Cek role pengguna
                            }
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
                }
            }

            binding.tvSignUp.setOnClickListener {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
        }
    }

    private fun saveSession(userId: String) {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", userId)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun checkUserRole(userId: String) {
        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val role = snapshot.child("role").getValue(String::class.java)
                role?.let {
                    if (it == "admin") {
                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                    } else {
                        startActivity(Intent(this@LoginActivity, LandingpageActivity::class.java))
                    }
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error checking user role: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun writeToActivityHistory(email: String) {
        val activityRef = FirebaseDatabase.getInstance().getReference("riwayat_aktivitas").push()
        val currentTime = System.currentTimeMillis()
        val activityData = mapOf(
            "aktivitas" to "Login",
            "datetime" to currentTime,
            "email" to email
        )
        activityRef.setValue(activityData).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Riwayat aktivitas berhasil dicatat", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal mencatat riwayat aktivitas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
