package com.example.sweetsensations.Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.Auth.LoginActivity
import com.example.sweetsensations.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.KelolaHargaBuah.setOnClickListener {
            val intent = Intent(this, ActivityListKelolaBuah::class.java)
            startActivity(intent)
        }

        binding.KelolaPasar.setOnClickListener {
            val intent = Intent(this, ActivityListKelolaPasar::class.java)
            startActivity(intent)
        }

        binding.KelolaBerita.setOnClickListener {
            val intent = Intent(this, ActivityListKelolaBerita::class.java)
            startActivity(intent)
        }
        binding.buttonLogout.setOnClickListener {
            logoutAndNavigateToLogin(this) // Panggil fungsi logout
        }

    }

    private fun logoutAndNavigateToLogin(context: Context) {

        // Menghapus session di SharedPreferences
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_id") // Hapus UID pengguna
        editor.putBoolean("is_logged_in", false) // Ubah status login menjadi false
        editor.apply()

        // Sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Navigate to the login activity
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}