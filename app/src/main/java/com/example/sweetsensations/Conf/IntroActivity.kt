package com.example.sweetsensations.Conf

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.Auth.LoginActivity
import com.example.sweetsensations.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Menggunakan Handler untuk delay 2 detik
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Tutup IntroActivity agar tidak kembali ke sini
        }, 2000) // 2000 ms = 2 detik
    }
}
