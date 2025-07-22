package com.example.sweetsensations.Admin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityListKelolaBeritaBinding
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class ActivityListKelolaBerita : AppCompatActivity() {

    private lateinit var binding: ActivityListKelolaBeritaBinding
    private lateinit var beritaList: ArrayList<BeritaBuahData>
    private lateinit var database: DatabaseReference
    private var selectedImageBase64: String? = null // Variabel untuk menyimpan gambar terpilih
    private var ivPreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKelolaBeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tambahBerita.setOnClickListener {
            val intent = Intent(this, TambahBeritaBuah::class.java)
            startActivity(intent)
        }
        binding.BeritatoAdmin.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().getReference("berita_buah")
        beritaList = arrayListOf()
        fetchData()

        // Get data from Intent
        val beritaId = intent.getStringExtra("IDBerita") // Get ID
        val judulBerita = intent.getStringExtra("judulBerita") // Get title
        val kategoriBerita = intent.getStringExtra("kategoriBerita") // Get category
        val deskripsiBerita = intent.getStringExtra("deskripsiBerita") // Get description
        val waktuBerita = intent.getStringExtra("waktuBerita")
        val gambarBerita = intent.getStringExtra("gambarBerita")

        if (beritaId != null && judulBerita != null) {
            // If ID and title are available, display or process them
            handleEditBerita(beritaId, judulBerita, kategoriBerita, deskripsiBerita, waktuBerita, gambarBerita)
        }
        binding.listBerita.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ActivityListKelolaBerita)
        }
    }

    private fun handleEditBerita(
        beritaId: String,
        judulBerita: String,
        kategoriBerita: String?,
        deskripsiBerita: String?,
        waktuBerita: String?,
        gambarBerita: String?
    ) {
        val builder = AlertDialog.Builder(this@ActivityListKelolaBerita)
        val dialogView = layoutInflater.inflate(R.layout.activity_update_berita, null)
        builder.setView(dialogView)

        val etJudulBerita = dialogView.findViewById<EditText>(R.id.et_title)
        val spinnerKategoriBerita = dialogView.findViewById<Spinner>(R.id.spinner_category)
        val etDeskripsiBerita = dialogView.findViewById<EditText>(R.id.et_description)
        val ivPreview = dialogView.findViewById<ImageView>(R.id.iv_preview_update)
        val btnUploadBerita = dialogView.findViewById<ImageButton>(R.id.btn_upload_berita)
        val btnUpdate = dialogView.findViewById<Button>(R.id.btn_update)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel_berita)

        etJudulBerita.setText(judulBerita)
        etDeskripsiBerita.setText(deskripsiBerita)

        gambarBerita?.let {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            ivPreview.setImageBitmap(bitmap)
            selectedImageBase64 = it
        } ?: run {
            ivPreview.setImageResource(R.drawable.ic_launcher_background)
        }

        val kategoriBeritaArray = arrayOf(
            "Buah Lokal", "Buah Impor", "Buah Organik", "Manfaat Buah", "Tips Membeli Buah"
        )
        val kategoriAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kategoriBeritaArray
        )
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategoriBerita.adapter = kategoriAdapter

        kategoriBerita?.let {
            val position = kategoriAdapter.getPosition(it)
            if (position >= 0) spinnerKategoriBerita.setSelection(position)
        }

        btnUploadBerita.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        val dialog = builder.create()
        // Cancel Button: dismiss dialog when pressed
        btnCancel.setOnClickListener {
            dialog.dismiss() // Close the dialog when Cancel is pressed
        }

        // Update Button: update the data and dismiss dialog
        btnUpdate.setOnClickListener {
            val updatedTitle = etJudulBerita.text.toString().trim()
            val updatedCategory = spinnerKategoriBerita.selectedItem?.toString()?.trim() ?: ""
            val updatedDescription = etDeskripsiBerita.text.toString().trim()

            if (updatedTitle.isEmpty() || updatedCategory.isEmpty() || updatedDescription.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data sebelum memperbarui", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val updatedBerita = BeritaBuahData(
                judulBerita = updatedTitle,
                kategoriBerita = updatedCategory,
                deskripsiBerita = updatedDescription,
                waktuBerita = waktuBerita,
                gambar = selectedImageBase64,
                id = beritaId
            )

            database.child(beritaId).setValue(updatedBerita)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    builder.create().dismiss() // Close dialog after update success
                    fetchData() // Reload data
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        builder.create().show() // Show the dialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                ivPreview?.setImageBitmap(bitmap) // Menggunakan ivPreview yang sudah diinisialisasi

                // Konversi bitmap ke Base64 untuk penyimpanan
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                selectedImageBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal memuat gambar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    beritaList.clear()
                    for (beritaSnapshot in snapshot.children) {
                        val berita = beritaSnapshot.getValue(BeritaBuahData::class.java)
                        beritaList.add(berita!!)
                    }
                }
                val adapter = KelolaBeritaAdapter(beritaList)
                binding.listBerita.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ActivityListKelolaBerita,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }
}
