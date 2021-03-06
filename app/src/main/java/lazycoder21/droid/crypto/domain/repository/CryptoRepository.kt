package lazycoder21.droid.crypto.domain.repository

import androidx.lifecycle.LiveData
import lazycoder21.droid.crypto.domain.model.CryptoDetail
import lazycoder21.droid.crypto.utils.CryptoSortingUtils.SortOptions
import lazycoder21.droid.crypto.utils.CryptoSortingUtils.SortOrder

interface CryptoRepository {
    fun getCryptoListings(
        symbol: String,
        @SortOrder sortOrder: Int,
        @SortOptions sortOption: Int
    ): LiveData<List<CryptoDetail>>

    fun getFavouriteCryptoListings(
        symbol: String,
        @SortOrder sortOrder: Int,
        @SortOptions sortOption: Int
    ): LiveData<List<CryptoDetail>>

    fun getCryptoDetail(symbol: String): LiveData<CryptoDetail>

    suspend fun syncListing()
    suspend fun favouriteCrypto(item: CryptoDetail)
}