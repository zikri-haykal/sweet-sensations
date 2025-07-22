package com.example.sweetsensations.Masyarakat.Fix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Admin.PasarData
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.databinding.ActivityListPasarBuahBinding
import com.google.firebase.database.*

class ListUserPasar : AppCompatActivity() {
    private lateinit var binding: ActivityListPasarBuahBinding
    private lateinit var database: DatabaseReference
    private lateinit var pasarList: ArrayList<PasarData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPasarBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("pasar_buah")
        pasarList = arrayListOf()

        fetchData()

        // Configure RecyclerView
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListUserPasar)
        }
        binding.btnKembali.setOnClickListener {
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pasarList.clear()
                if (snapshot.exists()) {
                    for (pasarSnap in snapshot.children) {
                        val pasar = pasarSnap.getValue(PasarData::class.java)
                        if (pasar != null) { // Null safety check
                            pasarList.add(pasar)
                        }
                    }
                }
                val userRole = "masyarakat" // Ganti dengan peran pengguna yang sebenarnya, seperti dari sesi login
                val adapter = PasarUserAdapter(pasarList, userRole)
                binding.recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListUserPasar, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
