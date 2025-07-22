package com.example.sweetsensations.Masyarakat.Quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityQuizSatuBinding

class QuizSatu : AppCompatActivity() {
    private lateinit var binding: ActivityQuizSatuBinding
    private var currentQuestionIndex = 0
    private var score = 0
    private val answers = mutableListOf<Int>() // Menyimpan jawaban user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizSatuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        updateQuestion()
    }

    private fun setupListeners() {
        // Tombol Back
        binding.btnKembali.setOnClickListener {
            onBackPressed()
        }

        // Tombol Submit
        binding.btnSubmit.setOnClickListener {
            if (isAnswerSelected()) {
                checkAnswer()
                if (currentQuestionIndex < 3) { // Masih ada pertanyaan berikutnya
                    currentQuestionIndex++
                    updateQuestion()
                } else {
                    showSummary()
                }
            } else {
                Toast.makeText(this, "Silahkan pilih jawaban terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // Reset radio group saat pindah pertanyaan
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.btnSubmit.isEnabled = true
        }
    }

    private fun updateQuestion() {
        // Update nomor pertanyaan
        binding.tvQuestionNumber.text = "Pertanyaan ${currentQuestionIndex + 1}/4"

        // Update progress bar
        binding.progressBar.progress = ((currentQuestionIndex + 1) * 25)

        // Reset radio group
        binding.radioGroup.clearCheck()
        binding.btnSubmit.isEnabled = false

        // Update pertanyaan dan pilihan berdasarkan currentQuestionIndex
        when (currentQuestionIndex) {
            0 -> {
                binding.tvQuestion.text = "Buah apakah yang memiliki julukan 'King of Fruits' di Asia Tenggara?"
                binding.ivQuestion.setImageResource(R.drawable.durian)
                binding.rbOption1.text = "Durian"
                binding.rbOption2.text = "Mangga"
                binding.rbOption3.text = "Rambutan"
                binding.rbOption4.text = "Manggis"
            }
            1 -> {
                binding.tvQuestion.text = "Buah manggis memiliki julukan apa?"
                binding.ivQuestion.setImageResource(R.drawable.manggis)
                binding.rbOption1.text = "Queen of Fruits"
                binding.rbOption2.text = "Princess of Fruits"
                binding.rbOption3.text = "Emperor of Fruits"
                binding.rbOption4.text = "Lady of Fruits"
            }
            2 -> {
                binding.tvQuestion.text = "Dari manakah asal buah rambutan?"
                binding.ivQuestion.setImageResource(R.drawable.rambutan)
                binding.rbOption1.text = "Malaysia"
                binding.rbOption2.text = "Indonesia"
                binding.rbOption3.text = "Thailand"
                binding.rbOption4.text = "Vietnam"
            }
            3 -> {
                binding.tvQuestion.text = "Berapa rata-rata kadar air dalam buah semangka?"
                binding.ivQuestion.setImageResource(R.drawable.watermeloen)
                binding.rbOption1.text = "92%"
                binding.rbOption2.text = "85%"
                binding.rbOption3.text = "78%"
                binding.rbOption4.text = "65%"
            }
        }
    }

    private fun isAnswerSelected(): Boolean {
        return binding.radioGroup.checkedRadioButtonId != -1
    }

    private fun checkAnswer() {
        val selectedAnswerId = binding.radioGroup.checkedRadioButtonId
        val correctAnswers = listOf(
            R.id.rbOption1, // Jawaban benar soal 1
            R.id.rbOption1, // Jawaban benar soal 2
            R.id.rbOption2, // Jawaban benar soal 3
            R.id.rbOption1  // Jawaban benar soal 4
        )

        if (selectedAnswerId == correctAnswers[currentQuestionIndex]) {
            score++
        }

        // Simpan jawaban user
        answers.add(selectedAnswerId)
    }

    private fun showSummary() {
        val intent = Intent(this, HasilQuiz::class.java).apply {
            putExtra("SCORE", score)
            putIntegerArrayListExtra("ANSWERS", ArrayList(answers))
        }
        startActivity(intent)
        finish()
    }
}