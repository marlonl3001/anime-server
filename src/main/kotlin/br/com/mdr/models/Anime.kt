package br.com.mdr.models

import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    val id: Int,
    val name: String
)
