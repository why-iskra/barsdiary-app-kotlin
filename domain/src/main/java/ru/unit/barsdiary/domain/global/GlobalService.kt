package ru.unit.barsdiary.domain.global

import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo

interface GlobalService {
    suspend fun getBirthdays(): BirthdaysPojo
    suspend fun getInBox(page: Int): BoxPojo
    suspend fun getOutBox(page: Int): BoxPojo
    suspend fun getInBoxCount(): Int
    suspend fun removeInBoxMessages(list: List<Int>): Boolean
    suspend fun removeOutBoxMessages(list: List<Int>): Boolean
    suspend fun markRead(id: Int)
}