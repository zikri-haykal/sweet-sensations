package com.example.sweetsensations.Masyarakat.Fix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Admin.BeritaBuahData
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.databinding.ActivityListBeritaBuahBinding
import com.google.firebase.database.*

class ListUserBerita : AppCompatActivity() {
    private lateinit var binding: ActivityListBeritaBuahBinding
    private lateinit var database: DatabaseReference
    private lateinit var beritaList: ArrayList<BeritaBuahData>
    private lateinit var fullBeritaList: ArrayList<BeritaBuahData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBeritaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi
        database = FirebaseDatabase.getInstance().getReference("berita_buah")
        beritaList = arrayListOf()
        fullBeritaList = arrayListOf()

        // Setup RecyclerView
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListUserBerita)
        }

        // Setup Spinner
        setupSpinner()

        // Setup Back Button
        binding.btnBackBerita.setOnClickListener {
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }

        // Fetch Data
        fetchData()
    }

    private fun setupSpinner() {
        val kategoriBerita = arrayOf(
            "Semua Kategori",
            "Buah Lokal",
            "Buah Impor",
            "Buah Organik",
            "Manfaat Buah",
            "Tips Membeli Buah"
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kategoriBerita
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerFilter.adapter = spinnerAdapter
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterBerita(kategoriBerita[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak perlu implementasi
            }
        }
    }

    private fun filterBerita(kategori: String) {
        if (kategori == "Semua Kategori") {
            updateRecyclerView(fullBeritaList)
        } else {
            val filteredList = fullBeritaList.filter { berita ->
                berita.kategoriBerita == kategori
            }
            updateRecyclerView(filteredList)
        }
    }

    private fun updateRecyclerView(list: List<BeritaBuahData>) {
        beritaList.clear()
        beritaList.addAll(list)
        val userRole = "masyarakat"
        val adapter = BeritaUserAdapter(beritaList, userRole)
        binding.recyclerView.adapter = adapter
    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullBeritaList.clear()
                if (snapshot.exists()) {
                    for (beritaSnap in snapshot.children) {
                        val berita = beritaSnap.getValue(BeritaBuahData::class.java)
                        if (berita != null) {
                            fullBeritaList.add(berita)
                        }
                    }
                    // Inisialisasi dengan semua data
                    updateRecyclerView(fullBeritaList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ListUserBerita,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}