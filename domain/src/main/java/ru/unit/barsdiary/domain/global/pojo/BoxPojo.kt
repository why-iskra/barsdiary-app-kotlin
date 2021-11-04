package ru.unit.barsdiary.domain.global.pojo

data class BoxPojo(
    val count: Int,
    val newCount: Int,
    val items: List<MessagePojo>,
)