package com.example.sweetsensations.Masyarakat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sweetsensations.Auth.LoginActivity
import com.example.sweetsensations.Masyarakat.BuahMatang.ListBuahMatangActivity
import com.example.sweetsensations.Masyarakat.Fix.ListUserBerita
import com.example.sweetsensations.Masyarakat.Fix.ListUserHarga
import com.example.sweetsensations.Masyarakat.Fix.ListUserPasar
import com.example.sweetsensations.Masyarakat.Prediksi.PrediksiActivity
import com.example.sweetsensations.Masyarakat.Quiz.QuizSatu
import com.example.sweetsensations.Masyarakat.RiwayatPrediksi.ListRiwayatPrediksi
import com.example.sweetsensations.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LandingpageActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textUserWelcome: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landingpage)

        // Daftar warna yang akan digunakan secara berurutan
        val colors = listOf(
            "#E24C5B", // Merah terang
            "#C71A35", // Merah
            "#B51729", // Merah gelap
            "#8CAF50", // Hijau cerah
            "#698D3A", // Hijau tua
            "#E5A149"  // Oranye terang
        )

        // Daftar ID card yang akan dimodifikasi
        val cardIds = listOf(
            R.id.card1,
            R.id.card2,
            R.id.card3,
            R.id.card4,
            R.id.card5,
            R.id.card6
        )

        // Iterasi melalui setiap ID card dan warna secara berurutan
        for (i in cardIds.indices) {
            // Tangkap ConstraintLayout berdasarkan ID
            val card = findViewById<ConstraintLayout>(cardIds[i])

            // Pilih warna berurutan dari daftar
            val color =
                colors[i % colors.size] // Menggunakan modulus untuk menghindari IndexOutOfBoundsException

            // Membuat GradientDrawable baru
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = 20f // Radius sudut sesuai keinginan
            drawable.setColor(Color.parseColor(color)) // Set warna latar belakang

            // Terapkan drawable sebagai latar belakang card
            card.background = drawable
        }

        // Tangkap ImageView dengan ID imageBuahMatang
        val imageBuahMatang = findViewById<ImageView>(R.id.imageBuahMatang)

        // Set onClickListener pada ImageView
        imageBuahMatang.setOnClickListener {
            // Intent ke ListBuahMatangActivity
            val intent = Intent(this, ListBuahMatangActivity::class.java)
            startActivity(intent)
        }

        // Tangkap ImageView dengan ID imageBuahMatang
        val Quiz = findViewById<ImageView>(R.id.imageQuiz)

        // Set onClickListener pada ImageView
        Quiz.setOnClickListener {
            // Intent ke ListBuahMatangActivity
            val intent = Intent(this, QuizSatu::class.java)
            startActivity(intent)
        }

        val imagePrediksi = findViewById<ImageView>(R.id.imagePrediksi)
        imagePrediksi.setOnClickListener {
            val intent = Intent(this, PrediksiActivity::class.java)
            startActivity(intent)
        }

        val lihatHargaBuah = findViewById<ImageView>(R.id.imageHargaBuah)
        lihatHargaBuah.setOnClickListener {
            val intent = Intent(this, ListUserHarga::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<ImageView>(R.id.buttonProfile)
        profileButton.setOnClickListener {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)// Panggil fungsi logout
        }

        val lihatBeritaBuah = findViewById<ImageView>(R.id.imageBeritaBuah)
        lihatBeritaBuah.setOnClickListener {
            val intent = Intent(this, ListUserBerita::class.java)
            startActivity(intent)
        }

        val lihatPasarBuah = findViewById<ImageView>(R.id.imagePasar)
        lihatPasarBuah.setOnClickListener {
            val intent = Intent(this, ListUserPasar::class.java)
            startActivity(intent)

        }

        val teksPasarBuah = findViewById<TextView>(R.id.tvLihatSemuaPasar)
        teksPasarBuah.setOnClickListener {
            val intent = Intent(this, ListUserPasar::class.java)
            startActivity(intent)
        }

        val teksRiwayatPrediksi = findViewById<TextView>(R.id.tvLihatSemuaPrediksi)
        val imageRiwayatPrediksi = findViewById<ImageView>(R.id.ivRiwayatPrediksi)

        teksRiwayatPrediksi.setOnClickListener {
            val intent = Intent(this, ListRiwayatPrediksi::class.java)
            startActivity(intent)
        }

        imageRiwayatPrediksi.setOnClickListener {
            val intent = Intent(this, ListRiwayatPrediksi::class.java)
            startActivity(intent)
        }

        textUserWelcome = findViewById(R.id.textUserWelcome)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        getUserFullName()
    }

    private fun getUserFullName() {
        val currentUserEmail = auth.currentUser?.email
        if (currentUserEmail != null) {
            database.orderByChild("email").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                val fullname =
                                    userSnapshot.child("fullname").getValue(String::class.java)
                                textUserWelcome.text = "Welcome, $fullname!"
                            }
                        } else {
                            Log.d("LandingpageActivity", "User not found.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("LandingpageActivity", "Error: ${error.message}")
                    }
                })
        } else {
            textUserWelcome.text = "Welcome, Guest!"
        }
    }
}
