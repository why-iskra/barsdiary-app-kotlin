package ru.unit.barsdiary.ui.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.sdk.Constants

class BoxPagingSource(
    private val globalUseCase: GlobalUseCase,
    private val isInBox: Boolean,
) : PagingSource<Int, MessagePojo>() {

    override fun getRefreshKey(state: PagingState<Int, MessagePojo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessagePojo> {
        val page = params.key ?: 1

        return try {
            lateinit var result: BoxPojo
            withContext(Dispatchers.IO) {
                result = if (isInBox) globalUseCase.getInBox(page) else globalUseCase.getOutBox(page)
            }

            val prevKey = params.key?.minus(1)
            LoadResult.Page(
                data = result.items,
                prevKey = if (prevKey == null || prevKey == 0) null else prevKey,
                nextKey = if (result.items.size < Constants.MAIL_BOX_PAGE_SIZE) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
