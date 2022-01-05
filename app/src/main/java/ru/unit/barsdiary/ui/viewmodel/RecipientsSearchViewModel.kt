package ru.unit.barsdiary.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import ru.unit.barsdiary.domain.global.GlobalUseCase
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo
import ru.unit.barsdiary.ui.paging.source.RecipientsSearchPagingSource
import ru.unit.barsdiary.sdk.Constants

class RecipientsSearchViewModel @AssistedInject constructor(
    private val globalUseCase: GlobalUseCase,
    @Assisted private val type: Int,
    @Assisted private val searchTextLiveData: LiveData<String>,
) : ViewModel() {

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            type: Int,
            searchTextLiveData: LiveData<String>
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(type, searchTextLiveData) as T
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(type: Int, searchTextLiveData: LiveData<String>): RecipientsSearchViewModel
    }

    @ExperimentalPagingApi
    val searchResultFlow: Flow<PagingData<FoundUserPojo>> = Pager(
        config = PagingConfig(pageSize = Constants.SEARCH_USER_PAGE_SIZE, prefetchDistance = 5),
        pagingSourceFactory = { RecipientsSearchPagingSource(type = type, searchText = searchTextLiveData.value ?: "", globalUseCase = globalUseCase) }
    ).flow.cachedIn(viewModelScope)

}