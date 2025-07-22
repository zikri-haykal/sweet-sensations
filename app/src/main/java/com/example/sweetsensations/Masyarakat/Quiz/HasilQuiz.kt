package com.example.sweetsensations.Masyarakat.Quiz

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sweetsensations.Masyarakat.LandingpageActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityHasilQuizBinding

class HasilQuiz : AppCompatActivity() {
    private lateinit var binding: ActivityHasilQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("SCORE", 0)
        val jawaban = intent.getIntegerArrayListExtra("ANSWERS") ?: arrayListOf()

        setupSummaryView(score)
        setupListeners()
    }

    private fun setupSummaryView(score: Int) {
        // Hitung persentase
        val nilaiBulat = (score.toFloat() / 4 * 100).toInt()

        binding.apply {
            // Update skor
            tvScore.text = "$score/4"
            tvPercentage.text = "$nilaiBulat%"

            // Update status
            when {
                nilaiBulat >= 75 -> {
                    tvStatus.text = "Selamat! Anda Lulus"
                    tvStatus.setTextColor(ContextCompat.getColor(this@HasilQuiz, R.color.green))
                    ivResult.setImageResource(R.drawable.check)
                }
                else -> {
                    tvStatus.text = "Maaf, Anda Belum Lulus"
                    tvStatus.setTextColor(ContextCompat.getColor(this@HasilQuiz, R.color.red))
                    ivResult.setImageResource(R.drawable.cancel)
                }
            }

            // Update feedback berdasarkan skor
            tvFeedback.text = when {
                score == 4 -> "Sempurna! Anda sangat memahami tentang buah-buahan!"
                score == 3 -> "Bagus! Pengetahuan Anda tentang buah-buahan sudah baik!"
                score == 2 -> "Cukup baik, tapi masih perlu belajar lebih banyak."
                score == 1 -> "Jangan menyerah, terus pelajari lebih banyak tentang buah-buahan!"
                else -> "Anda perlu belajar lebih banyak tentang buah-buahan."
            }
        }
    }

    private fun setupListeners() {
        binding.btnSelesai.setOnClickListener {
            // Kembali ke halaman utama
            val intent = Intent(this, LandingpageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.btnUlangiQuiz.setOnClickListener {
            // Kembali ke halaman quiz
            val intent = Intent(this, QuizSatu::class.java)
            startActivity(intent)
            finish()
        }
    }
}