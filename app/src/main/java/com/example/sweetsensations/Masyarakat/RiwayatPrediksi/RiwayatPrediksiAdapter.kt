package com.example.sweetsensations.Masyarakat.RiwayatPrediksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.databinding.CardRiwayatBinding

class RiwayatPrediksiAdapter(private val listRiwayatPrediksi: ArrayList<PrediksiData>) :
    RecyclerView.Adapter<RiwayatPrediksiAdapter.ViewHolder>() {
    class ViewHolder(val binding: CardRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardRiwayatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listRiwayatPrediksi.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listRiwayatPrediksi[position]
        holder.apply {
            binding.apply {
                val formattedAcidity = when (currentItem.acidity) {
                    1 -> "Sangat Tidak Asam"
                    2 -> "Tidak Asam"
                    3 -> "Netral"
                    4 -> "Asam"
                    5 -> "Sangat Asam"
                    else -> "Tidak Diketahui"
                }

                val formattedSweetness = when (currentItem.sweetness) {
                    1 -> "Sangat Tidak Manis"
                    2 -> "Tidak Manis"
                    3 -> "Netral"
                    4 -> "Manis"
                    5 -> "Sangat Manis"
                    else -> "Tidak Diketahui"
                }

                val formattedCrunchiness = when (currentItem.crunchiness) {
                    1 -> "Sangat Tidak Renyah"
                    2 -> "Tidak Renyah"
                    3 -> "Netral"
                    4 -> "Renyah"
                    5 -> "Sangat Renyah"
                    else -> "Tidak Diketahui"
                }

                val formattedJuiciness = when (currentItem.juiciness) {
                    1 -> "Sangat Tidak Berair"
                    2 -> "Tidak Berair"
                    3 -> "Netral"
                    4 -> "Berair"
                    5 -> "Sangat Berair"
                    else -> "Tidak Diketahui"
                }

                val formattedRipeness = when (currentItem.ripeness) {
                    1 -> "Sangat Mentah"
                    2 -> "Mentah"
                    3 -> "Setengah Matang"
                    4 -> "Matang"
                    5 -> "Sangat Matang"
                    else -> "Tidak Diketahui"
                }

                val formattedSize = when (currentItem.size) {
                    1 -> "Sangat Kecil"
                    2 -> "Kecil"
                    3 -> "Sedang"
                    4 -> "Besar"
                    5 -> "Sangat Besar"
                    else -> "Tidak Diketahui"
                }

                val formattedWeight = when (currentItem.ripeness) {
                    1 -> "Sangat Ringan"
                    2 -> "Ringan"
                    3 -> "Sedang"
                    4 -> "Berat"
                    5 -> "Sangat Berat"
                    else -> "Tidak Diketahui"
                }

                val formattedResult = when (currentItem.result) {
                    "Bad" -> "Hasil Prediksi Anda Buruk"
                    "Good" -> "Hasil Prediksi Anda Baik"
                    else -> "Hasil Prediksi Tidak Diketahui"
                }

// Format waktu
                val inputFormat = java.text.SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                    java.util.Locale.getDefault()
                )
                val outputFormat =
                    java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
                val date = inputFormat.parse(currentItem.created_at)
                val formattedDate = outputFormat.format(date)

// Menampilkan hasil
                tvEmail.text = currentItem.email
                tvCreatedAt.text = "Dilakukan pada: $formattedDate"
                tvAcidity.text = formattedAcidity
                tvSize.text = formattedSize
                tvWeight.text = formattedWeight
                tvSweetness.text = formattedSweetness
                tvCrunchiness.text = formattedCrunchiness
                tvJuiciness.text = formattedJuiciness
                tvRipeness.text = formattedRipeness
                tvResult.text = formattedResult


            }
        }
    }

}