package com.example.sweetsensations.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.databinding.ActivityTambahPasarBuahBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TambahPasarBuah : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPasarBuahBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPasarBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackListPasar.setOnClickListener{
            intent = Intent(this, ActivityListKelolaPasar::class.java)
        }

        database = FirebaseDatabase.getInstance().getReference("pasar_buah")

        binding.btnSubmit.setOnClickListener{
            saveData()
        }

    }

    private fun saveData() {
        val namaPasar = binding.etNamaPasar.text.toString()
        val alamatPasar = binding.etAlamatPasar.text.toString()
        val jamOperasional = binding.etJamOperasional.text.toString()
        val linkGambar = binding.etLinkPasar.text.toString()

        if (namaPasar.isEmpty() && alamatPasar.isEmpty() && jamOperasional.isEmpty() || linkGambar.isEmpty()){
            binding.etNamaPasar.error = "Nama Pasar Tidak Boleh Kosong"
            binding.etAlamatPasar.error = "Alamat Pasar Tidak Boleh Kosong"
            binding.etJamOperasional.error = "Jam Operasional Tidak Boleh Kosong"
            binding.etLinkPasar.error = "Link Gambar Tidak Boleh Kosong"
        }
        val pasarId = database.push().key!!
        val pasarData = PasarData(namaPasar, alamatPasar, jamOperasional, linkGambar, pasarId)
        database.child(pasarId).setValue(pasarData)
            .addOnCompleteListener{
                Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                binding.etNamaPasar.text.clear()
                binding.etAlamatPasar.text.clear()
                binding.etJamOperasional.text.clear()
                binding.etLinkPasar.text.clear()
                val intent = Intent(this, ActivityListKelolaPasar::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{err->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}