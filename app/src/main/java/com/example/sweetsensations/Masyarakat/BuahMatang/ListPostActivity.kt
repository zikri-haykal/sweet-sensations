package com.example.sweetsensations.Masyarakat.BuahMatang

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListPostActivity : AppCompatActivity() {

    private lateinit var buahMatangList: ArrayList<BuahMatangData> // Fixed: Changed List to ArrayList for mutability
    private lateinit var database: DatabaseReference
    lateinit var binding: ActivityListPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("buah_matang")
        buahMatangList = arrayListOf()

        // Setup RecyclerView
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListPostActivity)
        }

        val id = intent.getStringExtra("id")
        val caption = intent.getStringExtra("caption")
        val madeby = intent.getStringExtra("madeby")
        val createdAt = intent.getStringExtra("created_at")
        val email = intent.getStringExtra("email")

        Log.d("IntentData", "ID: $id, Caption: $caption, MadeBy: $madeby, CreatedAt: $createdAt, Email: $email")

        if (id != null && caption != null && madeby != null && createdAt != null && email != null) {
            showUpdateModal(id, caption, madeby, createdAt)
        } else {
            Log.e("ModalError", "Data Intent tidak lengkap!")
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
    }

    private fun showUpdateModal(id: String, caption: String, madeby: String, createdAt: String) {
        // Buat builder untuk AlertDialog
        val builder = AlertDialog.Builder(this@ListPostActivity)

        // Inflate layout custom untuk dialog
        val dialogView = layoutInflater.inflate(R.layout.activity_update_post, null)

        // Inisialisasi elemen dari layout custom
        val etCaption = dialogView.findViewById<EditText>(R.id.et_caption)
        val tvMadeby = dialogView.findViewById<TextView>(R.id.tv_madeby)
        val tvCreatedAt = dialogView.findViewById<TextView>(R.id.tv_created_at)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btn_update)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        // Set data ke elemen dialog
        etCaption.setText(caption)
        tvMadeby.text = "Dibuat oleh: $madeby"
        tvCreatedAt.text = "Dibuat pada: $createdAt"

        // Set view custom ke AlertDialog.Builder
        builder.setView(dialogView)

        // Buat AlertDialog dari builder
        val dialog = builder.create()

        // Aksi tombol update
        btnUpdate.setOnClickListener {
            val updatedCaption = etCaption.text.toString()

            // Validasi input
            if (updatedCaption.isEmpty()) {
                Toast.makeText(this, "Caption tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update data ke Firebase
            database.child(id).child("caption").setValue(updatedCaption)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal update data: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Aksi tombol cancel
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Tampilkan dialog
        dialog.show()
        Log.d("DialogTest", "Dialog ditampilkan")
    }


    private fun fetchBuahMatangData() {
        // Mendapatkan email pengguna yang sedang login
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        if (currentUserEmail != null) {
            // Mengambil data dari Firebase
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    buahMatangList.clear()  // Menghapus data lama
                    if (snapshot.exists()) {
                        for (buahMatangSnap in snapshot.children) {
                            val buahMatang = buahMatangSnap.getValue(BuahMatangData::class.java)
                            if (buahMatang != null) {
                                // Memfilter data berdasarkan email pengguna yang sedang login
                                if (buahMatang.email == currentUserEmail) {
                                    buahMatangList.add(buahMatang)  // Menambahkan data jika email sesuai
                                }
                            }
                        }

                        // Periksa apakah data lebih dari satu
                        Log.d("ListBuahMatang", "Jumlah data: ${buahMatangList.size}")

                        // Set adapter dan beri tahu perubahan data
                        val adapter = PostAdapter(buahMatangList)
                        binding.recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()  // Memberitahukan adapter bahwa data sudah berubah
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ListPostActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this@ListPostActivity, "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show()
        }
    }

}