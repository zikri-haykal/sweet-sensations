package com.example.sweetsensations.Masyarakat.BuahMatang

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sweetsensations.databinding.ActivityCardPostBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class PostAdapter(private val buahMatangList: List<BuahMatangData>) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    class ViewHolder(val binding: ActivityCardPostBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ActivityCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return buahMatangList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = buahMatangList[position]
        holder.apply {
            binding.apply {
                tvCaption.text = currentItem.caption
                tvUploadTime.text = currentItem.created_at
                tvSenderName.text = currentItem.madeby

                // Menambahkan klik listener pada item
                btnEdit.setOnClickListener {
                    val intent = Intent(holder.itemView.context, ListPostActivity::class.java).apply {
                        putExtra("id", currentItem.id) // Kirim ID unik
                        putExtra("caption", currentItem.caption) // Kirim caption
                        putExtra("madeby", currentItem.madeby) // Kirim madeby
                        putExtra("created_at", currentItem.created_at) // Kirim waktu
                        putExtra("email", currentItem.email) // Kirim waktu
                    }
                    holder.itemView.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Apakah Anda yakin ingin menghapus postingan ini?")
                        .setPositiveButton("Ya") { dialog, _ ->
                            val database = FirebaseDatabase.getInstance().getReference("buah_matang")
                            database.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(holder.itemView.context, "Berhasil menghapus postingan", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(holder.itemView.context, "Gagal karena ${error.message}", Toast.LENGTH_SHORT).show()

                                }
                            dialog.dismiss() // Menutup dialog setelah aksi selesai
                        }
                        .setNegativeButton("Batal") { dialog, _ ->
                            dialog.dismiss() // Menutup dialog jika dibatalkan
                        }
                        .show() // Menampilkan dialog
                }

            }
        }
    }


}