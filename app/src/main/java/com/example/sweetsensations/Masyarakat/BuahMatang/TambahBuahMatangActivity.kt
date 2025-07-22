package com.example.sweetsensations.Masyarakat.BuahMatang

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityTambahBuahMatangBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class TambahBuahMatangActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var binding : ActivityTambahBuahMatangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBuahMatangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("buah_matang")

        binding.btnSubmit.setOnClickListener {
            saveData()
        }

        // Ambil ImageButton dengan ID btnBack
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Set klik listener untuk tombol back
        btnBack.setOnClickListener {
            // Intent ke ListBuahMatangActivity
            val intent = Intent(this, ListBuahMatangActivity::class.java)
            startActivity(intent)
            finish() // Mengakhiri aktivitas ini agar tidak berada di back stack
        }
    }

    private fun saveData() {
        val caption = binding.etCaption.text.toString()

        // Validasi input caption
        if (caption.isEmpty()) {
            binding.etCaption.error = "Masukkan Caption"
            return
        }

        // Mendapatkan waktu saat ini dalam format terbaca
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTime))

        // Mendapatkan email pengguna yang login
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        if (currentUserEmail != null) {
            // Query untuk mendapatkan fullname berdasarkan email
            FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Mendapatkan fullname
                            var fullname: String? = null
                            for (userSnapshot in snapshot.children) {
                                fullname = userSnapshot.child("fullname").getValue(String::class.java)
                            }

                            if (fullname != null) {
                                // Membuat ID unik untuk data
                                val buahMatangId = database.push().key!!

                                // Membuat objek data dengan urutan yang benar
                                val buahMatang = BuahMatangData(
                                    caption = caption,
                                    created_at = formattedDate, // Menyimpan tanggal saat ini sebagai createdAt
                                    email = currentUserEmail,  // Menyimpan email pengguna
                                    id = buahMatangId,         // Menyimpan ID unik
                                    madeby = fullname          // Menyimpan fullname
                                )

                                // Menyimpan data ke database
                                database.child(buahMatangId).setValue(buahMatang)
                                    .addOnCompleteListener {
                                        // Menampilkan pesan sukses
                                        Toast.makeText(this@TambahBuahMatangActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                                        // Intent ke ListBuahMatangActivity setelah data berhasil disimpan
                                        val intent = Intent(this@TambahBuahMatangActivity, ListBuahMatangActivity::class.java)
                                        startActivity(intent)
                                        finish() // Optional: To close the current activity
                                    }
                                    .addOnFailureListener {
                                        // Menampilkan pesan error
                                        Toast.makeText(this@TambahBuahMatangActivity, "Data gagal ditambahkan, Karena ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this@TambahBuahMatangActivity, "Fullname tidak ditemukan untuk email ini", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@TambahBuahMatangActivity, "Pengguna dengan email ini tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@TambahBuahMatangActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

}
