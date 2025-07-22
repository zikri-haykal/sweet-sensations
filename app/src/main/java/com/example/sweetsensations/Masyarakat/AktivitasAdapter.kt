package com.example.sweetsensations.Masyarakat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.databinding.ActivityHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AktivitasAdapter(private val listAktivitas: ArrayList<AktivitasData>) :
    RecyclerView.Adapter<AktivitasAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ActivityHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listAktivitas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listAktivitas[position]
        holder.apply {
            binding.apply {
                activityName.text = currentItem.aktivitas
                // Ambil timestamp dari currentItem
                val timestamp = currentItem.datetime

            // Format timestamp menjadi format yang bisa dibaca manusia
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(timestamp))

            // Menampilkan hasil format waktu ke dalam TextView
                activityTime.text = formattedDate
            }
        }
    }

}