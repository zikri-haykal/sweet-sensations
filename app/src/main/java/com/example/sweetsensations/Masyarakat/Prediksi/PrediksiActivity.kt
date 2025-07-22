package com.example.sweetsensations.Masyarakat.Prediksi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.ActivityPrediksiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class PrediksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrediksiBinding
    private val BASE_URL = "https://bccb-125-165-108-184.ngrok-free.app" // Simpan URL di variabel konstanta
    private val PREDICT_ENDPOINT = "$BASE_URL/predict"
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrediksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("riwayat_prediksi")
        auth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener{
            onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            predict()
        }
    }

    private fun predict() {
        try {

            val jsonObject = JSONObject().apply {
                put("Size", binding.spinnerUkuran.selectedItem.toString().toInt())
                put("Weight", binding.spinnerBerat.selectedItem.toString().toInt())
                put("Sweetness", binding.spinnerManis.selectedItem.toString().toInt())
                put("Crunchiness", binding.spinnerKerenyahan.selectedItem.toString().toInt())
                put("Juiciness", binding.spinnerJuicy.selectedItem.toString().toInt())
                put("Ripeness", binding.spinnerKematangan.selectedItem.toString().toInt())
                put("Acidity", binding.spinnerAsam.selectedItem.toString().toInt())
            }

            val requestBody = jsonObject.toString()
            Log.d("Request Body", requestBody)

            val jsonObjectRequest = object : JsonObjectRequest(
                Method.POST, PREDICT_ENDPOINT, jsonObject,
                Response.Listener { response ->
                    try {
                        val data = response.getInt("quality")
                        Log.d("API Response", response.toString())

                        if (data == 1) {
                            // Tampilkan dialog prediksi berhasil
                            val builder = AlertDialog.Builder(this@PrediksiActivity)
                            val dialogView = layoutInflater.inflate(R.layout.good_prediction, null)
                            builder.setView(dialogView)
                            val dialog = builder.create()
                            dialog.show()

                            // Simpan hasil ke Firebase
                            simpanRiwayat(jsonObject, "Good")
                        } else {
                            // Tampilkan dialog prediksi buruk
                            val builder = AlertDialog.Builder(this@PrediksiActivity)
                            val dialogView = layoutInflater.inflate(R.layout.bad_prediction, null)
                            builder.setView(dialogView)
                            val dialog = builder.create()
                            dialog.show()

                            //  menyimpan ke Firebase untuk prediksi buruk
                            simpanRiwayat(jsonObject, "Bad")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        showToast("Error parsing response")
                    }
                },
                Response.ErrorListener { error ->
                    showToast("Request error: ${error.message}")
                }) {
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }

            val queue = Volley.newRequestQueue(this)
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Unexpected error occurred: ${e.message}")
        }
    }

    private fun simpanRiwayat(inputData: JSONObject, result: String) {
        val predictionId = database.push().key // Buat unique ID
        // Dapatkan waktu saat ini dalam format ISO 8601
        val currentTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())

        // Ambil email pengguna yang sedang login
        val currentUserEmail = auth.currentUser?.email ?: "Unknown User"

        val predictionData = mapOf(
            "size" to inputData.getInt("Size"),
            "weight" to inputData.getInt("Weight"),
            "sweetness" to inputData.getInt("Sweetness"),
            "crunchiness" to inputData.getInt("Crunchiness"),
            "juiciness" to inputData.getInt("Juiciness"),
            "ripeness" to inputData.getInt("Ripeness"),
            "acidity" to inputData.getInt("Acidity"),
            "result" to result,
            "created_at" to currentTimestamp,
            "email" to currentUserEmail // Menambahkan email pengguna
        )

        if (predictionId != null) {
            database.child(predictionId).setValue(predictionData)
                .addOnSuccessListener {
                    showToast("Prediction saved successfully")
                }
                .addOnFailureListener { error ->
                    showToast("Failed to save prediction: ${error.message}")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

