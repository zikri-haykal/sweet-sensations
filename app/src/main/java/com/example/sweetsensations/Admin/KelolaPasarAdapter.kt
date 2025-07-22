package com.example.sweetsensations.Admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.CardKelolaPasarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class KelolaPasarAdapter(private val listPasar: ArrayList<PasarData>) :
    RecyclerView.Adapter<KelolaPasarAdapter.ViewHolder>() {
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
        return listPasar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listPasar[position]
        holder.apply {
            binding.tvMarketName.text = currentItem.namaPasar
            binding.tvMarketAddress.text = currentItem.alamatPasar
            binding.tvOperatingHours.text = currentItem.jamOperasional

            // Load image using Glide
            Glide.with(holder.itemView.context)  // Menggunakan holder.itemView.context untuk context
                .load(currentItem.urlGambar)  // Menggunakan urlGambar dari currentItem
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground))
                .into(binding.ivMarketImage)  // Menggunakan ivGambarBuah dari binding

            binding.apply {
                btnEdit.setOnClickListener {
                    val intent =
                        Intent(holder.itemView.context, ActivityListKelolaPasar::class.java)
                    intent.putExtra("id", currentItem.id)
                    intent.putExtra("namaPasar", currentItem.namaPasar)
                    intent.putExtra("alamatPasar", currentItem.alamatPasar)
                    intent.putExtra("jamOperasional", currentItem.jamOperasional)
                    intent.putExtra("linkGambar", currentItem.urlGambar)
                    holder.itemView.context.startActivity(intent)
                }
                btnDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Konfirmasi Hapus Pasar")
                        .setMessage("Apakah Anda yakin ingin menghapus pasar ini?")
                        .setPositiveButton("Ya") { _, _ ->
                            val database = FirebaseDatabase.getInstance().getReference("pasar_buah")
                            database.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    // Menghapus item dari list lokal dan memberi tahu adapter
                                    listPasar.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Pasar berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { exception ->
                                    // Menampilkan pesan kesalahan dari exception
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Gagal menghapus pasar: ${exception.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        .setNegativeButton("Tidak") { _, _ ->
                            Toast.makeText(
                                holder.itemView.context,
                                "Batal menghapus pasar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .show()
                }
            }

        }
    }
}