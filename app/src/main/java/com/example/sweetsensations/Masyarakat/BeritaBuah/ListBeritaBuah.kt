package com.example.sweetsensations.Masyarakat.BeritaBuah

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListBeritaBuahBinding

class ListBeritaBuah : AppCompatActivity() {

    private lateinit var binding : ActivityListBeritaBuahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBeritaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val back = findViewById<ImageButton>(R.id.btnBackBerita)
        back.setOnClickListener{
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }

    }
}