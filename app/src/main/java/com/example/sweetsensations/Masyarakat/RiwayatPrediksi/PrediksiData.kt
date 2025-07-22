package com.example.sweetsensations.Masyarakat.RiwayatPrediksi

data class PrediksiData(
    val size: Int? = null,
    val weight: Int = 0,
    val sweetness: Int = 0,
    val crunchiness: Int = 0,
    val juiciness: Int = 0,
    val ripeness: Int = 0,
    val acidity: Int = 0,
    val result: String = "",
    val created_at: String = "",
    val email: String = ""
)
