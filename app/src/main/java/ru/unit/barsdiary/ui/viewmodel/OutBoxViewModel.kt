package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.ui.paging.source.BoxPagingSource
import ru.unit.barsdiary.sdk.Constants
import javax.inject.Inject

@HiltViewModel
class OutBoxViewModel @Inject constructor(
    private val globalUseCase: GlobalUseCase,
) : ViewModel() {

    @ExperimentalPagingApi
    val outBoxFlow: Flow<PagingData<MessagePojo>> = Pager(
        config = PagingConfig(pageSize = Constants.MAIL_BOX_PAGE_SIZE, prefetchDistance = 5),
        pagingSourceFactory = { BoxPagingSource(isInBox = false, globalUseCase = globalUseCase) }
    ).flow.cachedIn(viewModelScope)

}