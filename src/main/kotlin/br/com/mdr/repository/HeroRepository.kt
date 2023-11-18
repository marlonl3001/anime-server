package br.com.mdr.repository

import br.com.mdr.models.ApiResponse
import br.com.mdr.models.Hero

interface HeroRepository {
    val heroes: List<Hero>

    suspend fun getAllHeroes(actualPage: Int = 1, limit: Int = 4): ApiResponse
    suspend fun searchHeroes(name: String?): ApiResponse
}