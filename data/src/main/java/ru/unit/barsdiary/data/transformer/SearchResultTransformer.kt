package ru.unit.barsdiary.data.transformer

import ru.unit.barsdiary.domain.BaseTransformer
import ru.unit.barsdiary.domain.global.pojo.FoundUserPojo
import ru.unit.barsdiary.domain.global.pojo.SearchResultPojo
import ru.unit.barsdiary.sdk.response.GetUserProfileDTO
import ru.unit.barsdiary.sdk.response.GetUserProfilesResponseDTO
import javax.inject.Inject

class SearchResultTransformer @Inject constructor() :
    BaseTransformer<GetUserProfilesResponseDTO, SearchResultPojo> {
    override fun transform(value: GetUserProfilesResponseDTO): SearchResultPojo {
        return SearchResultPojo(
            value.count,
            value.items?.map { transform(it) } ?: emptyList()
        )
    }

    private fun transform(value: GetUserProfileDTO): FoundUserPojo {
        return FoundUserPojo(
            value.id,
            value.profileId,
            value.fullName,
            value.className,
            value.childFullName
        )
    }

}