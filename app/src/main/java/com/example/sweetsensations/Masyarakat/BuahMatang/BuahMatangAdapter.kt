package com.example.sweetsensations.Masyarakat.BuahMatang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.databinding.ActivityCardBuahMatangBinding

class BuahMatangAdapter(private val buahMatangList: List<BuahMatangData>) : RecyclerView.Adapter<BuahMatangAdapter.ViewHolder>() {

    class ViewHolder(val binding: ActivityCardBuahMatangBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ActivityCardBuahMatangBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return buahMatangList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = buahMatangList[position]
        holder.apply {
            binding.apply {
                tvCaption.text = currentItem.caption
                tvCaption.text = currentItem.caption
                tvUploadTime.text = currentItem.created_at
                tvSenderName.text = currentItem.madeby
            }
        }
    }
}