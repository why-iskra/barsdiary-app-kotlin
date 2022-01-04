package ru.unit.barsdiary.mvvm.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.domain.global.pojo.SearchResultPojo
import ru.unit.barsdiary.sdk.Constants

class RecipientsSearchPagingSource(
    private val globalUseCase: GlobalUseCase,
    private val type: Int,
    private val searchText: String
) : PagingSource<Int, FoundUserPojo>() {

    override fun getRefreshKey(state: PagingState<Int, FoundUserPojo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FoundUserPojo> {
        val page = params.key ?: 1

        return try {
            lateinit var result: SearchResultPojo
            withContext(Dispatchers.IO) {
                result = globalUseCase.searchUser(type, searchText, page)
            }

            val prevKey = params.key?.minus(1)
            LoadResult.Page(
                data = result.items,
                prevKey = if (prevKey == null || prevKey == 0) null else prevKey,
                nextKey = if (result.items.size < Constants.SEARCH_USER_PAGE_SIZE) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
