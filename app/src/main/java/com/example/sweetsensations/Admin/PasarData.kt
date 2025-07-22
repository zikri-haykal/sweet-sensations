package com.example.sweetsensations.Admin

data class PasarData(
    val namaPasar: String?=null,
    val alamatPasar: String?=null,
    val jamOperasional: String?=null,
    val urlGambar: String? = null,  // Menambahkan URL gambar dengan nilai default null
    val id: String?= null,
)
