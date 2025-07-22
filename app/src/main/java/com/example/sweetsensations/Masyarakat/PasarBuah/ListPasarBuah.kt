package com.example.sweetsensations.Masyarakat.PasarBuah

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListPasarBuahBinding


class ListPasarBuah : AppCompatActivity() {

    private lateinit var binding : ActivityListPasarBuahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPasarBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val back = findViewById<ImageButton>(R.id.btnKembali)
        back.setOnClickListener {
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }

    }
}