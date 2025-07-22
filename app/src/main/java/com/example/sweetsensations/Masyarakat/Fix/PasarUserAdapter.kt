package com.example.sweetsensations.Masyarakat.Fix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.Admin.PasarData
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.CardKelolaPasarBinding

class PasarUserAdapter(
    private val pasarList: ArrayList<PasarData>,
    private val userRole: String // Tambahkan parameter untuk role pengguna
) : RecyclerView.Adapter<PasarUserAdapter.ViewHolder>() {

    class ViewHolder(val binding: CardKelolaPasarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardKelolaPasarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return pasarList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = pasarList[position]
        holder.apply {
            binding.apply {
                // Set data ke TextView
                tvMarketName.text = currentItem.namaPasar
                tvMarketAddress.text = currentItem.alamatPasar
                tvOperatingHours.text = currentItem.jamOperasional
                // Load image using Glide
                Glide.with(holder.itemView.context)  // Menggunakan holder.itemView.context untuk context
                    .load(currentItem.urlGambar)  // Menggunakan urlGambar dari currentItem
                    .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground))
                    .into(binding.ivMarketImage)  // Menggunakan ivGambarBuah dari binding
                // Cek role pengguna
                if (userRole != "admin") {
                    btnEdit.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                } else {
                    btnEdit.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                }
            }
        }
    }
}
