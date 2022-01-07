package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.MessagePojo
import ru.unit.barsdiary.sdk.Constants
import ru.unit.barsdiary.ui.paging.source.BoxPagingSource

class BoxViewModel @AssistedInject constructor(
    private val globalUseCase: GlobalUseCase,
    @Assisted private val isInBox: Boolean
) : ViewModel() {

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            isInBox: Boolean
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(isInBox) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(isInBox: Boolean): BoxViewModel
    }

    @ExperimentalPagingApi
    val boxFlow: Flow<PagingData<MessagePojo>> = Pager(
        config = PagingConfig(pageSize = Constants.MAIL_BOX_PAGE_SIZE, prefetchDistance = 5),
        pagingSourceFactory = { BoxPagingSource(isInBox = isInBox, globalUseCase = globalUseCase) }
    ).flow.cachedIn(viewModelScope)

}