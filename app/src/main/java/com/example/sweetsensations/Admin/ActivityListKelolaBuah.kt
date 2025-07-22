package com.example.sweetsensations.Admin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListKelolaBuahBinding
import com.google.firebase.database.*

class ActivityListKelolaBuah : AppCompatActivity() {

    private lateinit var binding: ActivityListKelolaBuahBinding
    private lateinit var hargaList: ArrayList<HargaBuahData>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKelolaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up buttons to navigate to other activities
        binding.tambahBuah.setOnClickListener {
            val intent = Intent(this, TambahHargaBuah::class.java)
            startActivity(intent)
        }

        binding.BuahToAdmin.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        // Initialize Firebase database reference and the list to hold the data
        database = FirebaseDatabase.getInstance().getReference("harga_buah")
        hargaList = arrayListOf()

        // Set up RecyclerView for the list
        binding.listBuah.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ActivityListKelolaBuah)
        }

        // Fetch data from Firebase
        fetchData()

        // Handle intent data for editing
        handleIntentData()
    }

    private fun fetchData() {
        // Fetching data from Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Clear previous data to avoid duplicates
                    hargaList.clear()

                    // Loop through all the children in the snapshot and map to HargaBuahData
                    for (hargaSnapshot in snapshot.children) {
                        val harga = hargaSnapshot.getValue(HargaBuahData::class.java)
                        harga?.let {
                            hargaList.add(it) // Add to list only if data is valid
                        }
                    }
                }

                // Set the adapter to RecyclerView after fetching data
                val adapter = KelolaHargaAdapter(hargaList)
                binding.listBuah.adapter = adapter
                adapter.notifyDataSetChanged()  // Notify adapter that data has changed
            }

            override fun onCancelled(error: DatabaseError) {
                // Show a Toast message with context
                Toast.makeText(
                    this@ActivityListKelolaBuah,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun handleIntentData() {
        // Get data from Intent
        val id = intent.getStringExtra("id")
        val namaBuah = intent.getStringExtra("namaBuah")
        val harga = intent.getStringExtra("harga")
        val asal = intent.getStringExtra("asal")
        val linkGambar = intent.getStringExtra("linkGambar")

        // If ID and namaBuah are available, handle editing logic
        if (id != null && namaBuah != null) {
            handleEditHarga(id, namaBuah, harga, asal, linkGambar)
        }
    }

    private fun handleEditHarga(
        id: String,
        namaBuah: String,
        harga: String?,
        asal: String?,
        linkGambar: String?
    ) {
        // Create AlertDialog
        val builder = AlertDialog.Builder(this@ActivityListKelolaBuah)
        val dialogView = layoutInflater.inflate(R.layout.activity_update_harga, null)
        builder.setView(dialogView)

        val btnUpdate = dialogView.findViewById<Button>(R.id.btn_update_price)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel_price)
        val etNamaBuah = dialogView.findViewById<EditText>(R.id.et_fruit_name)
        val etLinkGambar = dialogView.findViewById<EditText>(R.id.linkGambar)
        val etHarga = dialogView.findViewById<EditText>(R.id.et_fruit_price)
        val etAsal = dialogView.findViewById<EditText>(R.id.et_fruit_origin)
        val ivGambarBuah = dialogView.findViewById<ImageView>(R.id.iv_fruit_image)

        etNamaBuah.setText(namaBuah)
        etHarga.setText(harga)
        etAsal.setText(asal)
        etLinkGambar.setText(linkGambar)

        // Load image using Glide
        Glide.with(this@ActivityListKelolaBuah)
            .load(linkGambar) // Ensure this URL is valid
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                    .error(R.drawable.ic_launcher_foreground) // Error image
            )
            .into(ivGambarBuah) // Set image to ImageView

        val dialog = builder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdate.setOnClickListener {
            val updatedNamaBuah = etNamaBuah.text.toString().trim()
            val updatedHarga = etHarga.text.toString().trim()
            val updatedAsal = etAsal.text.toString().trim()
            val updatedLinkGambar = etLinkGambar.text.toString().trim()

            if (updatedNamaBuah.isEmpty() || updatedHarga.isEmpty() || updatedAsal.isEmpty() || updatedLinkGambar.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data sebelum memperbarui", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val updatedHargaBuah = HargaBuahData(
                namaBuah = updatedNamaBuah,
                harga = updatedHarga,
                asal = updatedAsal,
                urlGambar = updatedLinkGambar,
                id = id
            )

            database.child(id).setValue(updatedHargaBuah)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    fetchData()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        dialog.show()

    }
}
