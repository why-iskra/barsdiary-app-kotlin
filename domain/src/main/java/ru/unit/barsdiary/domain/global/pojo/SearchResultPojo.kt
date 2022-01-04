package ru.unit.barsdiary.domain.global.pojo

data class SearchResultPojo(
    val count: Int,
    val items: List<FoundUserPojo>
)
