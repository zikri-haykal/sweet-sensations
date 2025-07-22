package com.example.sweetsensations.Masyarakat.Fix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Admin.HargaBuahData
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.databinding.ActivityListHargaBuahBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ListUserHarga : AppCompatActivity(){
    private lateinit var binding: ActivityListHargaBuahBinding
    private lateinit var hargaList: ArrayList<HargaBuahData>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListHargaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("harga_buah")
        hargaList = arrayListOf()
        fetchData()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }
        binding.btnBack.setOnClickListener {
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                hargaList.clear()
                if(snapshot.exists()){
                    for (hargaSnap in snapshot.children){
                        val harga = hargaSnap.getValue(HargaBuahData::class.java)
                        hargaList.add(harga!!)
                    }
                }
                val userRole = "masyarakat" // Ganti dengan peran pengguna yang sebenarnya, seperti dari sesi login
                val adapter = HargaUserAdapter(hargaList, userRole)
                binding.recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListUserHarga, "error : $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}