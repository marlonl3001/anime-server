package br.com.mdr

import br.com.mdr.repository.NEXT_PAGE_KEY
import br.com.mdr.repository.PREVIOUS_PAGE_KEY
import br.com.mdr.models.ApiResponse
import br.com.mdr.models.Hero
import br.com.mdr.repository.HeroRepositoryImpl
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `access root endpoint, assert correct information`() =
        testApplication {
            val response = client.get("/")
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            assertEquals(
                expected = "Welcome to Hero API!",
                actual = response.bodyAsText()
            )
        }

    @ExperimentalSerializationApi
    @Test
    fun `access all heroes endpoint, query all pages, assert correct information`() =
        testApplication {
            val heroRepository = HeroRepositoryImpl()
            val actualPage = 1
            val limit = 5
            val heroes = heroRepository.heroes
            val response = client.get("/heroes?page=$actualPage&limit=$limit")

            assertEquals(
                expected = HttpStatusCode.OK,
                actual = response.status
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            val expected = ApiResponse(
                success = true,
                message = "ok",
                prevPage = calculatePage(heroes = heroes, page = actualPage, limit = limit)["prevPage"],
                nextPage = calculatePage(heroes = heroes, page = actualPage, limit = limit)["nextPage"],
                heroes = provideHeroes(
                    heroes = heroes,
                    page = actualPage,
                    limit = limit
                ),
                lastUpdated = actual.lastUpdated
            )
            assertEquals(
                expected = expected,
                actual = actual
            )
        }

    @ExperimentalSerializationApi
    @Test
    fun `access all heroes endpoint, query non existing page number, assert error`() =
        testApplication {
            val response = client.get("/heroes?page=6")
            assertEquals(
                expected = HttpStatusCode.NotFound,
                actual = response.status
            )
            assertEquals(
                expected = "Page not found.",
                actual = response.bodyAsText()
            )
        }

    @ExperimentalSerializationApi
    @Test
    fun `access all heroes endpoint, query invalid page number, assert error`() =
        testApplication {
            val response = client.get("/heroes?page=invalid")
            assertEquals(
                expected = HttpStatusCode.BadRequest,
                actual = response.status
            )
            val expected = ApiResponse(
                success = false,
                message = "Only numbers allowed."
            )
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
            assertEquals(
                expected = expected,
                actual = actual
            )
        }

    @ExperimentalSerializationApi
    @Test
    fun `access search heroes endpoint, query hero name, assert single hero result`() =
        testApplication {
            val response = client.get("/heroes/search?name=sas")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
                .heroes.size
            assertEquals(expected = 1, actual = actual)
        }

    @ExperimentalSerializationApi
    @Test
    fun `access search heroes endpoint, query hero name, assert multiple heroes result`() =
        testApplication {
            val response = client.get("/heroes/search?name=sa")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
                .heroes.size
            assertEquals(expected = 4, actual = actual)
        }

    @ExperimentalSerializationApi
    @Test
    fun `access search heroes endpoint, query an empty text, assert empty list as a result`() =
        testApplication {
            val response = client.get("/heroes/search?name=")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
                .heroes
            assertEquals(expected = emptyList(), actual = actual)
        }

    @ExperimentalSerializationApi
    @Test
    fun `access search heroes endpoint, query non existing hero, assert empty list as a result`() =
        testApplication {
            val response = client.get("/heroes/search?name=unknown")
            assertEquals(expected = HttpStatusCode.OK, actual = response.status)
            val actual = Json.decodeFromString<ApiResponse>(response.bodyAsText())
                .heroes
            assertEquals(expected = emptyList(), actual = actual)
        }

    @ExperimentalSerializationApi
    @Test
    fun `access non existing endpoint,assert not found`() =
        testApplication {
            val response = client.get("/unknown")
            assertEquals(expected = HttpStatusCode.NotFound, actual = response.status)
            assertEquals(expected = "Page not found.", actual = response.bodyAsText())
        }

    private fun calculatePage(heroes: List<Hero>, page: Int, limit: Int): Map<String, Int?> {
        val allHeroes = divideHeroesList(
            heroes = heroes,
            limit = limit
        )

        //Throws an exception if user request a higher page than list size
        require(page <= allHeroes.size)

        val prevPage = if (page == 1) null else page - 1
        val nextPage = if (page == allHeroes.size) null else page.plus(1)

        return mapOf(PREVIOUS_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
    }

    private fun divideHeroesList(heroes: List<Hero>, limit: Int): List<List<Hero>> =
        heroes.windowed(
            size = limit,
            step = limit,
            partialWindows = true
        )

    private fun provideHeroes(heroes: List<Hero>, page: Int, limit: Int): List<Hero> {
        val allHeroes = divideHeroesList(
            heroes = heroes,
            limit = limit
        )

        require(page > 0 && page <= allHeroes.size)

        return allHeroes[page -1]
    }
}