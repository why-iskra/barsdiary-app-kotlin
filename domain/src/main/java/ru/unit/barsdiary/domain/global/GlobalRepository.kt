package ru.unit.barsdiary.domain.global

import ru.unit.barsdiary.domain.global.pojo.AdvertBoardPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo

interface GlobalRepository {
    suspend fun getBirthdays(): BirthdaysPojo?
    suspend fun setBirthdays(value: BirthdaysPojo)
    suspend fun clearBirthdays()

    suspend fun getInBox(page: Int): BoxPojo?
    suspend fun setInBox(value: BoxPojo, page: Int)
    suspend fun clearInBox()

    suspend fun getOutBox(page: Int): BoxPojo?
    suspend fun setOutBox(value: BoxPojo, page: Int)
    suspend fun clearOutBox()

    suspend fun getInBoxCount(): Int?
    suspend fun setInBoxCount(value: Int)
    suspend fun clearInBoxCount()

    suspend fun getAdvertBoard(): AdvertBoardPojo?
    suspend fun setAdvertBoard(value: AdvertBoardPojo)
    suspend fun clearAdvertBoard()
}