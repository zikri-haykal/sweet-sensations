package com.example.sweetsensations.Masyarakat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetsensations.Auth.LoginActivity
import com.example.sweetsensations.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : ComponentActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var aktivtasDatabase: DatabaseReference
    private lateinit var aktivitasList: ArrayList<AktivitasData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("users")
        aktivtasDatabase = FirebaseDatabase.getInstance().getReference("riwayat_aktivitas")
        aktivitasList = arrayListOf()

        fetchData()
        // Set the data for the profile
        setDataProfile()

        // Set up button actions
        binding.btnKembali.setOnClickListener {
            intent = Intent(this, LandingpageActivity::class.java)
            startActivity(intent)
        }
        binding.updateButton.setOnClickListener {
            updateDataProfile()
        }
        binding.logoutButton.setOnClickListener {
            logoutAndNavigateToLogin(this)
        }
        binding.historiAktivitas.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }
    }

    private fun fetchData() {
        // Dapatkan email pengguna yang sedang login
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        if (currentUserEmail == null) {
            Toast.makeText(this, "Gagal mendapatkan email pengguna.", Toast.LENGTH_SHORT).show()
            return
        }

        aktivtasDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                aktivitasList.clear()
                if (snapshot.exists()) {
                    for (aktivitasSnapshot in snapshot.children) {
                        val aktivitas = aktivitasSnapshot.getValue(AktivitasData::class.java)
                        // Periksa apakah email cocok
                        if (aktivitas != null && aktivitas.email == currentUserEmail) {
                            aktivitasList.add(aktivitas)
                        }
                    }
                }

                val adapter = AktivitasAdapter(aktivitasList)
                binding.historiAktivitas.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Function to handle profile update
    private fun updateDataProfile() {
        // Get the full name entered by the user
        val updatedFullName = binding.fullName.editText?.text.toString()

        // Get the current user's email (email can't be updated)
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        if (updatedFullName.isEmpty()) {
            // Show an error message if the full name is empty
            Toast.makeText(this, "Full name cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUserEmail != null) {
            // Fetch the user by email and update the full name
            database.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Loop through the snapshot to find the user
                            for (userSnapshot in snapshot.children) {
                                // Get the user's ID (to be used for updating the record)
                                val userId = userSnapshot.key

                                // Update the full name for the user
                                if (userId != null) {
                                    database.child(userId).child("fullname")
                                        .setValue(updatedFullName)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Inform the user that the update was successful
                                                Toast.makeText(
                                                    this@ProfileActivity,
                                                    "Profile updated successfully!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                // Handle any errors during update
                                                Toast.makeText(
                                                    this@ProfileActivity,
                                                    "Failed to update profile.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                        } else {
                            // If the user is not found in the database
                            Log.d("ProfileActivity", "User not found.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                        Log.e("ProfileActivity", "Error: ${error.message}")
                    }
                })
        }
    }


    // Function to set data for the profile based on the logged-in user's email
    private fun setDataProfile() {
        // Get the email of the currently logged-in user
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        // Check if the email exists (user is logged in)
        if (currentUserEmail != null) {
            // Query Firebase for the user data based on the email
            database.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Loop through the snapshot and get the user's full name and email
                            for (userSnapshot in snapshot.children) {
                                val fullname =
                                    userSnapshot.child("fullname").getValue(String::class.java)
                                val email = userSnapshot.child("email").getValue(String::class.java)

                                // Populate the TextInputLayout fields
                                binding.fullName.editText?.setText(fullname)
                                binding.email.editText?.setText(email)
                            }
                        } else {
                            // If the user is not found in the database
                            Log.d("ProfileActivity", "User not found.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                        Log.e("ProfileActivity", "Error: ${error.message}")
                    }
                })
        } else {
            // If no email is found (user is not logged in)
            binding.fullName.editText?.setText("Guest")
            binding.email.editText?.setText("No email available")
        }
    }

    // Function to log out and navigate to the login activity
    private fun logoutAndNavigateToLogin(context: Context) {
        // Menghapus session di SharedPreferences
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_id") // Hapus UID pengguna
        editor.putBoolean("is_logged_in", false) // Ubah status login menjadi false
        editor.apply()

        // Sign out from Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Navigate to the login activity
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
