package com.lifequest.app.ui

import com.lifequest.app.domain.quote.Quote
import com.lifequest.app.domain.quote.QuoteResult
import com.lifequest.app.testing.FakeQuoteRepository
import com.lifequest.app.testing.MainDispatcherRule
import com.lifequest.app.ui.quote.QuoteUiState
import com.lifequest.app.ui.quote.QuoteViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class QuoteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val quote = Quote("Talk is cheap. Show me the code.", "Linus Torvalds")

    @Test
    fun `success result becomes Success state`() {
        val viewModel = QuoteViewModel(FakeQuoteRepository(QuoteResult.Success(quote, fromCache = false)))

        assertEquals(QuoteUiState.Success(quote, offline = false), viewModel.uiState.value)
    }

    @Test
    fun `cached result is shown as offline`() {
        val viewModel = QuoteViewModel(FakeQuoteRepository(QuoteResult.Success(quote, fromCache = true)))

        assertEquals(QuoteUiState.Success(quote, offline = true), viewModel.uiState.value)
    }

    @Test
    fun `no network without cache becomes Error state`() {
        val viewModel = QuoteViewModel(FakeQuoteRepository(QuoteResult.Failure(QuoteResult.Reason.NO_NETWORK)))

        assertTrue(viewModel.uiState.value is QuoteUiState.Error)
    }

    @Test
    fun `refresh after an error can recover`() {
        val repository = FakeQuoteRepository(QuoteResult.Failure(QuoteResult.Reason.SERVER_ERROR))
        val viewModel = QuoteViewModel(repository)

        repository.result = QuoteResult.Success(quote, fromCache = false)
        viewModel.refresh()

        assertEquals(QuoteUiState.Success(quote, offline = false), viewModel.uiState.value)
        assertEquals(2, repository.calls)
    }
}
