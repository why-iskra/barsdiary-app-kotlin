package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.mvvm.paging.source.BoxPagingSource
import javax.inject.Inject

@HiltViewModel
class InBoxViewModel @Inject constructor(
    private val globalUseCase: GlobalUseCase,
) : ViewModel() {

    @ExperimentalPagingApi
    val inBoxFlow: Flow<PagingData<MessagePojo>> = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 5),
        pagingSourceFactory = { BoxPagingSource(isInBox = true, globalUseCase = globalUseCase) }
    ).flow.cachedIn(viewModelScope)

}