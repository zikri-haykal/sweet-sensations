package com.example.sweetsensations.Auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        binding.btnRegis.setOnClickListener {
            val email = binding.RegisEmail.text.toString()
            val password = binding.RegisPassword.text.toString()
            val confirmPassword = binding.RegisConfirmPassword.text.toString()
            val fullname = binding.RegisUsername.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && fullname.isNotEmpty()) {
                if (password == confirmPassword) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid ?: ""
                                val createdAt = SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss",
                                    Locale.getDefault()
                                ).format(Date())

                                // Menambahkan role default "masyarakat"
                                val user = UsersData(fullname, email, createdAt, role = "masyarakat")

                                // Menyimpan data ke Firebase
                                database.child(userId).setValue(user)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            // Menulis riwayat aktivitas registrasi
                                            writeToActivityHistory(email, "Registration")

                                            Toast.makeText(
                                                this,
                                                "Registration successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent =
                                                Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Failed to save user data: ${dbTask.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ToLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun writeToActivityHistory(email: String, activity: String) {
        val activityRef = FirebaseDatabase.getInstance().getReference("riwayat_aktivitas").push()
        val currentTime = System.currentTimeMillis()
        val activityData = mapOf(
            "aktivitas" to activity,
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
