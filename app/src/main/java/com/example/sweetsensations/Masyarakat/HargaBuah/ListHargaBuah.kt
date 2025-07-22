package com.example.sweetsensations.Masyarakat.HargaBuah

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.SumberHargaBuahData
import com.example.sweetsensations.databinding.ActivityListHargaBuahBinding

class ListHargaBuah : AppCompatActivity() {

    private lateinit var binding : ActivityListHargaBuahBinding
    private lateinit var adapter: HargaBuahAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListHargaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        setupRecyclerView()

        // Load data into RecyclerView
        loadData()

        // Inisialisasi tombol kembali
        val back = findViewById<ImageButton>(R.id.btnBack)
        back.setOnClickListener {
            // Menavigasi kembali ke activity sebelumnya
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val data = SumberHargaBuahData.buatSetData()
        adapter.updateData(data)  // Update adapter with the new data
    }

    private fun setupRecyclerView() {
        adapter = HargaBuahAdapter(emptyList())  // Initialize with empty list
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

}