package lazycoder21.droid.crypto.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import lazycoder21.droid.crypto.data.local.LocalDataBase
import lazycoder21.droid.crypto.data.mapper.CryptoListingMapper.mapRemoteToLocal
import lazycoder21.droid.crypto.data.mapper.CryptoListingMapper.mapToDomain
import lazycoder21.droid.crypto.data.remote.CryptoApi
import lazycoder21.droid.crypto.domain.model.CryptoDetail
import lazycoder21.droid.crypto.domain.repository.CryptoRepository
import lazycoder21.droid.crypto.utils.CryptoSortingUtils.SortOptions
import lazycoder21.droid.crypto.utils.CryptoSortingUtils.SortOrder
import lazycoder21.droid.crypto.utils.Utils.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CryptoRepositoryImpl @Inject constructor(
    db: LocalDataBase,
    private val cryptoApi: CryptoApi,
) : CryptoRepository {

    private val dao = db.cryptoDao

    override fun getCryptoListings(
        symbol: String,
        @SortOrder sortOrder: Int,
        @SortOptions sortOption: Int,
        config: PagedList.Config
    ): LiveData<PagedList<CryptoDetail>> {
        val cryptos =
            dao.getListings(symbol = symbol, sortOrder = sortOrder, sortOption = sortOption)
                .map { it.mapToDomain }
        return LivePagedListBuilder(cryptos, config).build()
    }

    override fun getCryptoDetail(symbol: String): LiveData<CryptoDetail> {
        return dao.getDetail(symbol = symbol).map { it.mapToDomain }
    }

    override suspend fun syncListing() {
        safeApiCall {
            val response = cryptoApi.getListings()
            val listings = response.body()?.mapRemoteToLocal
            if (response.isSuccessful && listings != null) {
                dao.insertOrUpdate(listings)
            }
        }
    }

    override suspend fun favouriteCrypto(item: CryptoDetail) {
        item.favourite = !item.favourite
        dao.update(symbol = item.symbol, favourite = if (item.favourite) 1 else 0)
    }

    override fun getFavouriteCryptoListings(
        symbol: String,
        sortOrder: Int,
        sortOption: Int,
        config: PagedList.Config,
    ): LiveData<PagedList<CryptoDetail>> {
        val cryptos = dao.getFavouriteListings(
            symbol = symbol,
            sortOrder = sortOrder,
            sortOption = sortOption,
        ).map { it.mapToDomain }
        return LivePagedListBuilder(cryptos, config).build()
    }
}