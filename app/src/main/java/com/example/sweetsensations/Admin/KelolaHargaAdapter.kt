package com.example.sweetsensations.Admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.CardKelolaHargaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class KelolaHargaAdapter(private val listHarga: ArrayList<HargaBuahData>) :
    RecyclerView.Adapter<KelolaHargaAdapter.ViewHolder>() {
    class ViewHolder(val binding: CardKelolaHargaBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardKelolaHargaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listHarga.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listHarga[position]
        holder.apply {
            binding.tvFruitName.text = currentItem.namaBuah
            binding.tvFruitPrice.text = currentItem.harga
            binding.tvFruitOrigin.text = currentItem.asal
            // Jika ada gambar, tambahkan ini kembali
            // binding.ivFruitImage.setImageResource(currentItem.image)

            // Load image using Glide
            Glide.with(holder.itemView.context)  // Menggunakan holder.itemView.context untuk context
                .load(currentItem.urlGambar)  // Menggunakan urlGambar dari currentItem
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground))
                .into(binding.ivFruitImage)  // Menggunakan ivGambarBuah dari binding

            // Set OnClickListener untuk tombol edit
            binding.btnEdit.setOnClickListener {
                val intent = Intent(holder.itemView.context, ActivityListKelolaBuah::class.java).apply {
                    // Kirim data melalui Intent
                    putExtra("id", currentItem.id) // ID buah
                    putExtra("namaBuah", currentItem.namaBuah) // Nama buah
                    putExtra("harga", currentItem.harga) // Harga buah
                    putExtra("asal", currentItem.asal) // Asal buah
                    putExtra("linkGambar", currentItem.urlGambar) // Asal buah
                    // Tambahkan data lain jika diperlukan
                }
                // Mulai activity dengan data
                holder.itemView.context.startActivity(intent)
            }
            binding.btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Konfirmasi Hapus Harga")
                    .setMessage("Apakah Anda yakin ingin menghapus harga ini?")
                    .setPositiveButton("Yes") { _, _ ->
                        val database = FirebaseDatabase.getInstance().getReference("harga_buah")
                        database.child(currentItem.id.toString()).removeValue()
                            .addOnSuccessListener {
                                // Menghapus item dari listHarga di adapter
                                listHarga.removeAt(position)
                                // Memberitahukan adapter bahwa item telah dihapus
                                notifyItemRemoved(position)
                                // Memberikan feedback kepada pengguna
                                Toast.makeText(holder.itemView.context, "Harga berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // Jika penghapusan gagal
                                Toast.makeText(holder.itemView.context, "Gagal menghapus harga", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        // Menutup dialog jika batal
                        dialog.dismiss()
                        // Memberikan feedback kepada pengguna
                        Toast.makeText(holder.itemView.context, "Batal menghapus harga", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

        }
    }

}