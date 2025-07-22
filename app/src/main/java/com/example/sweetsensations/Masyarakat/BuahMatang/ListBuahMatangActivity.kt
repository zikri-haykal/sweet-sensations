package com.example.sweetsensations.Masyarakat.BuahMatang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListBuahMatangBinding
import com.google.firebase.database.*

class ListBuahMatangActivity : AppCompatActivity() {

    private lateinit var buahMatangList: ArrayList<BuahMatangData> // Fixed: Changed List to ArrayList for mutability
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityListBuahMatangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBuahMatangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("buah_matang")
        buahMatangList = arrayListOf()

        // Setup RecyclerView
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListBuahMatangActivity)
        }

        // Inisialisasi tombol kembali
        val back = findViewById<ImageButton>(R.id.btnBack)

        // Set listener untuk tombol kembali
        back.setOnClickListener {
            // Menavigasi kembali ke activity sebelumnya
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }

        // Fetch data from Firebase
        fetchBuahMatangData()

        // Set klik listener untuk tombol btnAdd
        binding.btnAdd.setOnClickListener {
            // Intent ke TambahBuahMatangActivity
            val intent = Intent(this, TambahBuahMatangActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchBuahMatangData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                buahMatangList.clear()  // Menghapus data lama
                if (snapshot.exists()) {
                    for (buahMatangSnap in snapshot.children) {
                        val buahMatang = buahMatangSnap.getValue(BuahMatangData::class.java)
                        if (buahMatang != null) {
                            buahMatangList.add(buahMatang)  // Menambahkan data baru
                        }
                    }

                    // Periksa apakah data lebih dari satu
                    Log.d("ListBuahMatang", "Jumlah data: ${buahMatangList.size}")

                    // Set adapter dan beri tahu perubahan data
                    val adapter = BuahMatangAdapter(buahMatangList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()  // Memberitahukan adapter bahwa data sudah berubah
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListBuahMatangActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
