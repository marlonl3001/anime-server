package br.com.mdr.models

import kotlinx.serialization.Serializable

@Serializable
data class Hero(
    val id: Int,
    val anime: String,
    val name: String,
    val image: String,
    val about: String,
    val rating: Double,
    val power: Int,
    val month: String,
    val day: String,
    val abilities: List<String>,
    val family: List<String>,
    val natureTypes: List<String>
)

enum class Anime(val animeName: String) {
    NARUTO("Naruto"),
    ONE_PIECE("One Piece"),
    BLEACH("Bleach")
}
