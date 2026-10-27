package com.lifequest.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lifequest.app.data.remote.QuoteApi
import com.lifequest.app.data.remote.toDomain
import com.lifequest.app.domain.quote.Quote
import com.lifequest.app.domain.quote.QuoteRepository
import com.lifequest.app.domain.quote.QuoteResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

private val Context.quoteStore by preferencesDataStore(name = "quote_cache")

/**
 * Chiến lược cache-first:
 * 1. Đã có quote của hôm nay trong cache → trả luôn, không gọi mạng.
 * 2. Chưa có → gọi API, thành công thì lưu cache.
 * 3. Gọi API lỗi → trả quote cũ trong cache (đánh dấu fromCache), không có thì báo lỗi.
 */
class CachedQuoteRepository @Inject constructor(
    private val api: QuoteApi,
    @ApplicationContext private val context: Context,
    private val json: Json,
    private val clock: Clock,
) : QuoteRepository {

    override suspend fun getDailyQuote(forceRefresh: Boolean): QuoteResult {
        val today = LocalDate.now(clock).toEpochDay()
        val cached = readCache()
        if (!forceRefresh && cached != null && cached.epochDay == today) {
            return QuoteResult.Success(cached.quote, fromCache = false)
        }

        // Chỉ bắt lỗi mạng/HTTP cụ thể. Không catch (e: Exception) vì sẽ nuốt luôn
        // CancellationException, làm coroutine không hủy được khi rời màn hình.
        return try {
            val quote = api.randomQuote().toDomain()
            writeCache(CachedQuote(quote, today))
            QuoteResult.Success(quote, fromCache = false)
        } catch (e: IOException) {
            cached?.let { QuoteResult.Success(it.quote, fromCache = true) }
                ?: QuoteResult.Failure(QuoteResult.Reason.NO_NETWORK)
        } catch (e: HttpException) {
            cached?.let { QuoteResult.Success(it.quote, fromCache = true) }
                ?: QuoteResult.Failure(QuoteResult.Reason.SERVER_ERROR)
        }
    }

    private suspend fun readCache(): CachedQuote? {
        val raw = context.quoteStore.data.first()[KEY_QUOTE] ?: return null
        return try {
            json.decodeFromString<CachedQuote>(raw)
        } catch (e: SerializationException) {
            null
        }
    }

    private suspend fun writeCache(value: CachedQuote) {
        context.quoteStore.edit { it[KEY_QUOTE] = json.encodeToString(value) }
    }

    @Serializable
    private data class CachedQuote(val text: String, val author: String, val epochDay: Long) {
        constructor(quote: Quote, epochDay: Long) : this(quote.text, quote.author, epochDay)

        val quote: Quote get() = Quote(text, author)
    }

    private companion object {
        val KEY_QUOTE = stringPreferencesKey("daily_quote_v1")
    }
}
