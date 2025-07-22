package com.example.sweetsensations.Masyarakat.HargaBuah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.databinding.ActivityCardHargaBuahBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.Admin.HargaBuahData
import com.example.sweetsensations.R

class HargaBuahAdapter(private var hargaBuahList: List<HargaBuahData>) : RecyclerView.Adapter<HargaBuahAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityCardHargaBuahBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ActivityCardHargaBuahBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // Method to update the list
    fun updateData(newData: List<HargaBuahData>) {
        hargaBuahList = newData
        notifyDataSetChanged() // Notify adapter to refresh the list
    }

    override fun getItemCount(): Int {
        return hargaBuahList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = hargaBuahList[position]
        holder.binding.apply {
            tvNamaBuah.text = currentItem.namaBuah
            tvHargaBuah.text = currentItem.harga
            tvAsal.text = currentItem.asal

            // Load image using Glide
            Glide.with(holder.itemView.context)  // Menggunakan holder.itemView.context untuk context
                .load(currentItem.urlGambar)  // Menggunakan urlGambar dari currentItem
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground))
                .into(ivGambarBuah)  // Menggunakan ivGambarBuah dari binding
        }
    }

}