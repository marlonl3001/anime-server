package br.com.mdr.repository

import br.com.mdr.models.ApiResponse
import br.com.mdr.models.Hero

interface HeroRepository {
    val heroes: Map<Int, List<Hero>>

    val page1: List<Hero>
    val page2: List<Hero>
    val page3: List<Hero>
    val page4: List<Hero>

    suspend fun getAllHeroes(actualPage: Int = 1): ApiResponse
    suspend fun searchHeroes(name: String): ApiResponse
}