package com.expert.healthinfo.core.domain.usecase

import com.expert.healthinfo.core.data.Result
import com.expert.healthinfo.core.domain.model.Headlines
import com.expert.healthinfo.core.domain.repository.IheadlinesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for [HeadlinesInteractor].
 *
 * Tests verify that the interactor correctly delegates all calls
 * to the [IheadlinesRepository] and returns its results unchanged.
 */
class HeadlinesInteractorTest {

    // System Under Test
    private lateinit var headlinesInteractor: HeadlinesInteractor

    // Mock dependency
    private val repository: IheadlinesRepository = mock()

    @Before
    fun setUp() {
        headlinesInteractor = HeadlinesInteractor(repository)
    }

    // ── getAllHeadlines ────────────────────────────────────────────────────────

    @Test
    fun `getAllHeadlines with empty query returns loading then success`() = runTest {
        // Given
        val fakeHeadlines = listOf(
            Headlines("1", "Author A", "https://img.url/a", "Desc A", "Title A", false),
            Headlines("2", "Author B", "https://img.url/b", "Desc B", "Title B", false)
        )
        whenever(repository.getAllHeadlines(""))
            .thenReturn(flowOf(Result.Success(fakeHeadlines)))

        // When
        val result = headlinesInteractor.getAllHeadlines("").first()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(2, (result as Result.Success).data!!.size)
        assertEquals("Title A", result.data!![0].title)
        verify(repository).getAllHeadlines("")
    }

    @Test
    fun `getAllHeadlines with query delegates to repository`() = runTest {
        // Given
        val query = "health"
        val fakeHeadlines = listOf(
            Headlines("3", "Author C", null, "Health desc", "Health Title", false)
        )
        whenever(repository.getAllHeadlines(query))
            .thenReturn(flowOf(Result.Success(fakeHeadlines)))

        // When
        val result = headlinesInteractor.getAllHeadlines(query).first()

        // Then
        assertTrue(result is Result.Success)
        assertEquals("Health Title", (result as Result.Success).data!![0].title)
        verify(repository).getAllHeadlines(query)
    }

    @Test
    fun `getAllHeadlines returns error when repository fails`() = runTest {
        // Given
        val errorMessage = "Network Error"
        whenever(repository.getAllHeadlines(""))
            .thenReturn(flowOf(Result.Error(errorMessage)))

        // When
        val result = headlinesInteractor.getAllHeadlines("").first()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, result.message)
    }

    // ── getFavoriteHeadlines ──────────────────────────────────────────────────

    @Test
    fun `getFavoriteHeadlines returns list of favorite headlines`() = runTest {
        // Given
        val favorites = listOf(
            Headlines("10", "Fav Author", "https://img.url/fav", "Fav Desc", "Fav Title", true)
        )
        whenever(repository.getFavoriteHeadlines())
            .thenReturn(flowOf(favorites))

        // When
        val result = headlinesInteractor.getFavoriteHeadlines().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Fav Title", result[0].title)
        assertTrue(result[0].isFavorite == true)
        verify(repository).getFavoriteHeadlines()
    }

    @Test
    fun `getFavoriteHeadlines returns empty list when no favorites`() = runTest {
        // Given
        whenever(repository.getFavoriteHeadlines())
            .thenReturn(flowOf(emptyList()))

        // When
        val result = headlinesInteractor.getFavoriteHeadlines().first()

        // Then
        assertTrue(result.isEmpty())
    }

    // ── isHeadlineFavorite ────────────────────────────────────────────────────

    @Test
    fun `isHeadlineFavorite returns true for favorited headline`() = runTest {
        // Given
        val id = "headline-123"
        whenever(repository.isHeadlineFavorite(id))
            .thenReturn(flowOf(true))

        // When
        val result = headlinesInteractor.isHeadlineFavorite(id).first()

        // Then
        assertTrue(result)
        verify(repository).isHeadlineFavorite(id)
    }

    @Test
    fun `isHeadlineFavorite returns false for non-favorited headline`() = runTest {
        // Given
        val id = "headline-456"
        whenever(repository.isHeadlineFavorite(id))
            .thenReturn(flowOf(false))

        // When
        val result = headlinesInteractor.isHeadlineFavorite(id).first()

        // Then
        assertEquals(false, result)
    }

    // ── insertFavoriteHeadlines ───────────────────────────────────────────────

    @Test
    fun `insertFavoriteHeadlines delegates to repository`() = runTest {
        // Given
        val headline = Headlines("20", "Insert Author", null, "Desc", "Insert Title", true)

        // When
        headlinesInteractor.insertFavoriteHeadlines(headline)

        // Then
        verify(repository).insertFavoriteHeadlines(headline)
    }

    // ── deleteFavoriteHeadlines ───────────────────────────────────────────────

    @Test
    fun `deleteFavoriteHeadlines returns affected rows count`() = runTest {
        // Given
        val headline = Headlines("30", "Delete Author", null, "Desc", "Delete Title", false)
        whenever(repository.deleteFavoriteHeadlines(headline))
            .thenReturn(1)

        // When
        val rowsAffected = headlinesInteractor.deleteFavoriteHeadlines(headline)

        // Then
        assertEquals(1, rowsAffected)
        verify(repository).deleteFavoriteHeadlines(headline)
    }

    @Test
    fun `deleteFavoriteHeadlines returns 0 when headline not found`() = runTest {
        // Given
        val headline = Headlines("99", "Ghost Author", null, "Desc", "Ghost Title", false)
        whenever(repository.deleteFavoriteHeadlines(headline))
            .thenReturn(0)

        // When
        val rowsAffected = headlinesInteractor.deleteFavoriteHeadlines(headline)

        // Then
        assertEquals(0, rowsAffected)
    }
}
