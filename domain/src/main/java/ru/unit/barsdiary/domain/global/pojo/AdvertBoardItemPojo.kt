package ru.unit.barsdiary.domain.global.pojo

data class AdvertBoardItemPojo(
    val theme: String,
    val message: String?,
    val author: String?,
    val authorId: Int,
    val advertDate: String?,
    val school: String?,
    val fileUrl: String?,
    val id: Int,
)
