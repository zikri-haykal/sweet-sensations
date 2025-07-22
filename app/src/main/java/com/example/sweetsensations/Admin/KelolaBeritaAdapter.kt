package com.example.sweetsensations.Admin

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.CardKelolaBeritaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class KelolaBeritaAdapter(private val listBerita: ArrayList<BeritaBuahData>) :
    RecyclerView.Adapter<KelolaBeritaAdapter.ViewHolder>() {

    class ViewHolder(val binding: CardKelolaBeritaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardKelolaBeritaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listBerita.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listBerita[position]
        with(holder.binding) {
            tvNewsTitle.text = currentItem.judulBerita
            tvNewsCategory.text = currentItem.kategoriBerita
            tvNewsDescription.text = currentItem.deskripsiBerita
            tvPublishTime.text = currentItem.waktuBerita

            // Menampilkan gambar dari string Base64
            if (!currentItem.gambar.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(currentItem.gambar, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    ivNewsImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    ivNewsImage.setImageResource(R.drawable.ic_launcher_background) // Fallback image
                }
            } else {
                ivNewsImage.setImageResource(R.drawable.ic_launcher_background) // Fallback image
            }

            // Atur warna kategori
            val categoryColor = ContextCompat.getColor(
                holder.itemView.context,
                CategoryColorUtils.getCategoryColor(currentItem.kategoriBerita ?: "Default")
            )

            val drawable = GradientDrawable().apply {
                cornerRadius =
                    holder.itemView.context.resources.getDimension(R.dimen.category_chip_radius)
                setColor(categoryColor)
            }
            tvNewsCategory.background = drawable

            btnEdit.setOnClickListener {
                val intent = Intent(holder.itemView.context, ActivityListKelolaBerita::class.java).apply {
                    putExtra("IDBerita", currentItem.id)
                    putExtra("judulBerita", currentItem.judulBerita)
                    putExtra("kategoriBerita", currentItem.kategoriBerita)
                    putExtra("deskripsiBerita", currentItem.deskripsiBerita)
                    putExtra("waktuBerita", currentItem.waktuBerita)
                    putExtra("gambarBerita", currentItem.gambar)
                }
                holder.itemView.context.startActivity(intent)
            }

            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Konfirmasi Hapus Berita")
                    .setMessage("Apakah Anda yakin ingin menghapus berita ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        val database = FirebaseDatabase.getInstance().getReference("berita_buah")
                        currentItem.id?.let { id ->
                            database.child(id).removeValue()
                                .addOnSuccessListener {
                                    listBerita.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Berita berhasil dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Gagal menghapus berita",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }

                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(
                            holder.itemView.context,
                            "Batal menghapus berita",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .show()
            }
        }
    }

    object CategoryColorUtils {
        fun getCategoryColor(category: String): Int {
            return when (category) {
                "Buah Lokal" -> R.color.category_buah_lokal
                "Buah Impor" -> R.color.category_buah_impor
                "Buah Organik" -> R.color.category_buah_organik
                "Manfaat Buah" -> R.color.category_manfaat_buah
                "Tips Membeli Buah" -> R.color.category_tips_buah
                else -> R.color.category_buah_lokal
            }
        }
    }
}
