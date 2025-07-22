package com.example.sweetsensations.Admin

data class HargaBuahData(
    val namaBuah: String?=null,
    val harga: String?=null,
    val asal: String?=null,
    val urlGambar: String? = null,  // Menambahkan URL gambar dengan nilai default null
    val id: String? = null,
)

