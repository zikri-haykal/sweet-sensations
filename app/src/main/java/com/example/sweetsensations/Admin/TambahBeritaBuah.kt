package com.example.sweetsensations.Admin

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.databinding.ActivityTambahBeritaBuahBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Base64

class TambahBeritaBuah : AppCompatActivity() {

    private lateinit var binding: ActivityTambahBeritaBuahBinding
    private lateinit var database: DatabaseReference
    private val REQUEST_IMAGE_PICK = 1001
    private var selectedImageBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBeritaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("berita_buah")

        binding.btnSubmitBerita.setOnClickListener{
            saveData()
        }

        binding.BackListBerita.setOnClickListener{
            intent = Intent(this, ActivityListKelolaBerita::class.java)
            startActivity(intent)
        }

        binding.btnUploadBerita.setOnClickListener{
            uploadFromGallery()
        }

        // Data kategori untuk spinner
        val kategoriBerita = arrayOf("Buah Lokal", "Buah Impor", "Buah Organik", "Manfaat Buah", "Tips Membeli Buah")

        // Buat adapter untuk spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, kategoriBerita)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter ke spinner
        binding.spinnerKategoriBerita.adapter = adapter
    }

    private fun uploadFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            val imageUri = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)

                // Validasi ukuran gambar
                if (getImageSizeInKB(bitmap) > 150) {
                    Toast.makeText(this, "Ukuran gambar terlalu besar, maksimal 100KB", Toast.LENGTH_SHORT).show()
                } else {
                    selectedImageBase64 = encodeImageToBase64(bitmap)
                    binding.ivPreviewBerita.setImageBitmap(bitmap) // Tampilkan gambar pratinjau
                }
            }
        }
    }

    private fun getImageSizeInKB(bitmap: Bitmap): Int {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return byteArray.size / 1024 // Mengembalikan ukuran dalam KB
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    private fun saveData() {
        val judulBerita = binding.etJudulBerita.text.toString()
        val deskripsiBerita = binding.etDeskripsiBerita.text.toString()
        val kategoriBerita = binding.spinnerKategoriBerita.selectedItem.toString()

        if (judulBerita.isEmpty() || deskripsiBerita.isEmpty()) {
            if (judulBerita.isEmpty()) {
                binding.etJudulBerita.error = "Judul Berita Tidak Boleh Kosong"
            }
            if (deskripsiBerita.isEmpty()) {
                binding.etDeskripsiBerita.error = "Deskripsi Berita Tidak Boleh Kosong"
            }
            return
        }

        val currentTime = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(Date())
        val beritaId = database.push().key!!

        // Gunakan gambar hanya jika ada gambar yang dipilih
        val gambar = selectedImageBase64 ?: ""

        val beritaData = BeritaBuahData(
            judulBerita,
            deskripsiBerita,
            kategoriBerita,
            currentTime,
            beritaId,
            gambar // Jika gambar kosong, akan menyimpan string kosong
        )

        database.child(beritaId).setValue(beritaData)
            .addOnCompleteListener {
                Toast.makeText(this, "Berita Buah Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                binding.etJudulBerita.text.clear()
                binding.etDeskripsiBerita.text.clear()
                binding.ivPreviewBerita.setImageDrawable(null) // Hapus pratinjau gambar
                intent = Intent(this, ActivityListKelolaBerita::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

}