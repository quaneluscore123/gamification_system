package com.lifequest.app.data.remote

import com.lifequest.app.domain.quote.Quote
import kotlinx.serialization.Serializable
import retrofit2.http.GET

/** Retrofit tự sinh code gọi HTTP từ interface này. Hàm suspend nên không chặn main thread. */
interface QuoteApi {

    @GET("quotes/random")
    suspend fun randomQuote(): QuoteDto
}

/**
 * DTO (Data Transfer Object) khớp đúng với JSON của server:
 * {"id": 1, "quote": "...", "author": "..."}
 * Tách DTO khỏi model domain để server đổi tên field thì chỉ phải sửa ở tầng data.
 */
@Serializable
data class QuoteDto(
    val id: Int,
    val quote: String,
    val author: String,
)

fun QuoteDto.toDomain() = Quote(text = quote, author = author)
