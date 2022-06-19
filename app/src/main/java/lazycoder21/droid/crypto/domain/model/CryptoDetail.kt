package lazycoder21.droid.crypto.domain.model

import lazycoder21.droid.crypto.utils.SortOptions
import lazycoder21.droid.crypto.utils.SortOrder

data class CryptoDetail(
    val volume: String? = null,
    val symbol: String = "",
    val askPrice: String? = null,
    val at: Long? = null,
    val lowPrice: String? = null,
    val highPrice: String? = null,
    val openPrice: String? = null,
    val baseAsset: String? = null,
    val quoteAsset: String? = null,
    val bidPrice: String? = null,
    val lastPrice: String? = null,
    val id: Int? = null,
    var favourite: Boolean = false,
) {
    companion object {
        val CryptoDetail.priceChange: Float
            get() = run {
                val default = 0f
                val curr = lastPrice?.toFloatOrNull() ?: default
                val open = openPrice?.toFloatOrNull() ?: default
                (curr - open) / open * 100
            }

        fun List<CryptoDetail>.sort(@SortOptions sortOptions: Int, @SortOrder sortOrder: Int) =
            sortedWith { o1, o2 ->
                val sortingMultiplier = if (sortOrder == SortOrder.DESCENDING) -1 else 1
                return@sortedWith when (sortOptions) {
                    SortOptions.ALPHABETIC -> sortingMultiplier * o1.symbol.compareTo(o2.symbol)
                    SortOptions.PRICE_CHANGE -> sortingMultiplier * o1.priceChange.compareTo(o2.priceChange)
                    SortOptions.VOLUME -> sortingMultiplier * (o1.volume?.compareTo(
                        o2.volume ?: return@sortedWith 0
                    ) ?: 0)
                    else -> throw IllegalStateException("sort not implemented")
                }
            }
    }
}

