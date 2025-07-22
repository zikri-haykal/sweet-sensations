package com.example.sweetsensations.Masyarakat.RiwayatPrediksi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListRiwayatPrediksiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListRiwayatPrediksi : AppCompatActivity() {

    private lateinit var binding : ActivityListRiwayatPrediksiBinding
    private lateinit var riwayatList : ArrayList<PrediksiData>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRiwayatPrediksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("riwayat_prediksi")
        // Inisialisasi Firebase Authentication
        auth = FirebaseAuth.getInstance()
        riwayatList = arrayListOf()
        fetchData()
        val back = findViewById<ImageButton>(R.id.btnBackRiwayat)
        back.setOnClickListener{
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }
        binding.listRiwayat.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListRiwayatPrediksi)
        }
    }

    private fun fetchData() {
        // Mendapatkan email pengguna yang sedang login
        val currentUserEmail = auth.currentUser?.email

        if (currentUserEmail != null) {
            // Menambahkan ValueEventListener untuk mengambil data riwayat berdasarkan email
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    riwayatList.clear()
                    if (snapshot.exists()) {
                        for (riwayatSnap in snapshot.children) {
                            val riwayat = riwayatSnap.getValue(PrediksiData::class.java)
                            // Mengecek apakah email pada data sesuai dengan email pengguna yang login
                            if (riwayat?.email == currentUserEmail) {
                                riwayatList.add(riwayat!!)
                            }
                        }
                    }
                    val adapter = RiwayatPrediksiAdapter(riwayatList)
                    binding.listRiwayat.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ListRiwayatPrediksi, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}