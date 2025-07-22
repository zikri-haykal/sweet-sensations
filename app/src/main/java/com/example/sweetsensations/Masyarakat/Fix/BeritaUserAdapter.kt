package com.example.sweetsensations.Masyarakat.Fix

import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.Admin.BeritaBuahData
import com.example.sweetsensations.Admin.KelolaBeritaAdapter.CategoryColorUtils
import com.example.sweetsensations.R
import com.example.sweetsensations.databinding.CardKelolaBeritaBinding

class BeritaUserAdapter(
    private val beritaList: ArrayList<BeritaBuahData>,
    private val userRole: String // Tambahkan parameter untuk role pengguna
) : RecyclerView.Adapter<BeritaUserAdapter.ViewHolder>() {

    class ViewHolder(val binding: CardKelolaBeritaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardKelolaBeritaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return beritaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = beritaList[position]
        holder.apply {
            binding.apply {
                // Set data ke TextView
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
