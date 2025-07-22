package com.example.sweetsensations

import com.example.sweetsensations.Admin.HargaBuahData

class SumberHargaBuahData {
    companion object {
        fun buatSetData(): ArrayList<HargaBuahData> {
            val list = arrayListOf<HargaBuahData>()

            // Menambahkan data harga buah
            list.add(HargaBuahData("Alpukat", "Rp. 30.000,00 / Kg", "Asal dari Kota Malang", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Mangga", "Rp. 25.000,00 / Kg", "Asal dari Probolinggo", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Pisang", "Rp. 15.000,00 / Kg", "Asal dari Yogyakarta", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Apel", "Rp. 20.000,00 / Kg", "Asal dari Batu", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Durian", "Rp. 50.000,00 / Kg", "Asal dari Medan", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Semangka", "Rp. 10.000,00 / Kg", "Asal dari Lampung", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Pepaya", "Rp. 8.000,00 / Kg", "Asal dari Bogor", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))
            list.add(HargaBuahData("Nanas", "Rp. 12.000,00 / Kg", "Asal dari Subang", "https://cdn.rri.co.id/berita/66/images/1708307328068-a/td8ju20nhyholto.jpeg",""))

            return list
        }
    }
}
