package jg.coursework.customheroesapp.data.dto

data class User(
    val id: Int,
    val name: String,
    val login: String,
    val password: String,
    val phoneNumber: String,
    val avatarSourcePath: String,
    val type: String
)
