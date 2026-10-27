package com.lifequest.app.domain.quote

data class Quote(
    val text: String,
    val author: String,
)

/** Kết quả rõ ràng cho từng trường hợp, để UI không phải biết tới IOException hay HttpException. */
sealed interface QuoteResult {
    data class Success(val quote: Quote, val fromCache: Boolean) : QuoteResult
    data class Failure(val reason: Reason) : QuoteResult

    enum class Reason { NO_NETWORK, SERVER_ERROR }
}

interface QuoteRepository {
    /** Mỗi ngày chỉ gọi mạng một lần; `forceRefresh` = true để lấy câu mới ngay. */
    suspend fun getDailyQuote(forceRefresh: Boolean = false): QuoteResult
}
