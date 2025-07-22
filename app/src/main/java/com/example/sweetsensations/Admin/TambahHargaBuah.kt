package com.example.sweetsensations.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityTambahHargaBuahBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TambahHargaBuah : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityTambahHargaBuahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahHargaBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("harga_buah")

        binding.btnSubmit.setOnClickListener{
            SaveHargaBuah()
        }

        val back = findViewById<ImageButton>(R.id.BackListHarga)
        back.setOnClickListener {
            intent = Intent(this, ActivityListKelolaBuah::class.java)
        }
    }

    private fun SaveHargaBuah(){
        val namaBuah = binding.etNamaBuah.text.toString()
        val hargaBuah = binding.etHargaBuah.text.toString()
        val asalBuah = binding.etAsalBuah.text.toString()
        val gambarBuah = binding.etLinkBuah.text.toString()

        if (namaBuah.isEmpty() && hargaBuah.isEmpty() && asalBuah.isEmpty()){
            binding.etNamaBuah.error = "Nama Buah Tidak Boleh Kosong"
            binding.etHargaBuah.error = "Harga Buah Tidak Boleh Kosong"
            binding.etAsalBuah.error = "Asal Buah Tidak Boleh Kosong"
            binding.etLinkBuah.error = "Link Buah Tidak Boleh Kosong"

        }

        val hargaBuahId = database.push().key!!
        val hargaBuahData = HargaBuahData(namaBuah, hargaBuah, asalBuah, gambarBuah, hargaBuahId)
        database.child(hargaBuahId).setValue(hargaBuahData)
            .addOnCompleteListener{
                Toast.makeText(this, "Harga Buah Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                binding.etNamaBuah.text.clear()
                binding.etHargaBuah.text.clear()
                binding.etAsalBuah.text.clear()
                binding.etLinkBuah.text.clear()
                intent = Intent(this, ActivityListKelolaBuah::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
            }

    }

}