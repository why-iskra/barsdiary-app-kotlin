package ru.unit.barsdiary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo
import ru.unit.barsdiary.mvvm.paging.source.RecipientsSearchPagingSource
import ru.unit.barsdiary.sdk.Constants

class RecipientsSearchViewModel @AssistedInject constructor(
    private val globalUseCase: GlobalUseCase,
    @Assisted private val type: Int,
    @Assisted private val searchText: String,
) : ViewModel() {

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            type: Int,
            searchText: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(type, searchText) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(type: Int, searchText: String): RecipientsSearchViewModel
    }

    var search = searchText

    @ExperimentalPagingApi
    val searchResultFlow: Flow<PagingData<FoundUserPojo>> = Pager(
        config = PagingConfig(pageSize = Constants.SEARCH_USER_PAGE_SIZE, prefetchDistance = 5),
        pagingSourceFactory = { RecipientsSearchPagingSource(type = type, searchText = search, globalUseCase = globalUseCase) }
    ).flow.cachedIn(viewModelScope)

}