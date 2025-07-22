package com.example.sweetsensations.Conf

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        // Fix the typo: setOnClickListener instead of setOnCliclListener
        binding.tvTesting.setOnClickListener {
            // Set the value in the database
            database.child("message").setValue("Koneksi Database Berhasil")
                .addOnCompleteListener {
                    Toast.makeText(this, "Database Berhasil", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menghubungkan ke database", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
