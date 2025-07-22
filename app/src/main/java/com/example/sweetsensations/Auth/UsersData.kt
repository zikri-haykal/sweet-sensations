package com.example.sweetsensations.Auth

data class UsersData(
    val fullname: String,
    val email: String,
    val created_at: String,
    val role: String? = null
)
