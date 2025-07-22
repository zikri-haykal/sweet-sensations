package com.example.sweetsensations.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListKelolaPasarBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActivityListKelolaPasar : AppCompatActivity() {
    private lateinit var binding: ActivityListKelolaPasarBinding
    private lateinit var pasarList: ArrayList<PasarData>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKelolaPasarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tambahPasar.setOnClickListener {
            intent = Intent(this, TambahPasarBuah::class.java)
            startActivity(intent)
        }
        binding.PasarToAdmin.setOnClickListener {
            intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        database = FirebaseDatabase.getInstance().getReference("pasar_buah")
        pasarList = arrayListOf()
        binding.listPasar.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ActivityListKelolaPasar)
        }
        fetchData()

        val id = intent.getStringExtra("id")
        val namaPasar = intent.getStringExtra("namaPasar")
        val alamatPasar = intent.getStringExtra("alamatPasar")
        val jamOperasional = intent.getStringExtra("jamOperasional")
        val linkGambar = intent.getStringExtra("linkGambar")

        if (id != null && namaPasar != null) {
            handleEditPasar(id, namaPasar, alamatPasar, jamOperasional, linkGambar)
        }

    }

    private fun handleEditPasar(
        id: String, namaPasar: String, alamatPasar: String?, jamOperasional: String?, linkGambar: String?
    ) {
        val builder = AlertDialog.Builder(this@ActivityListKelolaPasar)
        val dialogView = layoutInflater.inflate(R.layout.activity_update_pasar, null)
        builder.setView(dialogView)

        val btnUpdate = dialogView.findViewById<Button>(R.id.btn_update)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val etNamaPasar = dialogView.findViewById<EditText>(R.id.et_market_name)
        val etAlamatPasar = dialogView.findViewById<EditText>(R.id.et_market_address)
        val etLinkGambar = dialogView.findViewById<EditText>(R.id.etLinkGambar)
        val etJamOperasional = dialogView.findViewById<EditText>(R.id.et_operating_hours)
        val ivPreview = dialogView.findViewById<ImageView>(R.id.iv_preview)

        etNamaPasar.setText(namaPasar)
        etAlamatPasar.setText(alamatPasar)
        etJamOperasional.setText(jamOperasional)
        etLinkGambar.setText(linkGambar)

        Glide.with(this@ActivityListKelolaPasar)
            .load(linkGambar) // Ensure this URL is valid
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                    .error(R.drawable.ic_launcher_foreground) // Error image
            )
            .into(ivPreview) // Set image to ImageView

        val dialog = builder.create()
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdate.setOnClickListener {
            val updatedNamaPasar = etNamaPasar.text.toString().trim()
            val updatedAlamatPasar = etAlamatPasar.text.toString().trim()
            val updatedJamOperasional = etJamOperasional.text.toString().trim()
            val updatedLinkGambar = etLinkGambar.text.toString().trim()

            if (updatedNamaPasar.isEmpty() || updatedAlamatPasar.isEmpty() || updatedJamOperasional.isEmpty() || updatedLinkGambar.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data sebelum memperbarui", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val updatedPasar = PasarData(
                namaPasar = updatedNamaPasar,
                alamatPasar = updatedAlamatPasar,
                jamOperasional = updatedJamOperasional,
                urlGambar = updatedLinkGambar,
                id = id
            )
            database.child(id).setValue(updatedPasar).addOnSuccessListener {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                fetchData()

            }.addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()

    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pasarList.clear()  // Kosongkan list sebelum menambahkan data baru
                    for (pasarSnapshot in snapshot.children) {
                        val pasar = pasarSnapshot.getValue(PasarData::class.java)
                        pasar?.let {
                            pasarList.add(it)  // Tambahkan data baru ke pasarList
                        }
                    }
                }
                // Buat dan set adapter setelah data diperbarui
                val adapter = KelolaPasarAdapter(pasarList)
                binding.listPasar.adapter = adapter
                adapter.notifyDataSetChanged()  // Memberitahukan adapter bahwa data sudah berubah
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ActivityListKelolaPasar,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

}