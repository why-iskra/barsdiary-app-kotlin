package ru.unit.barsdiary.domain.global

import ru.unit.barsdiary.domain.global.pojo.*

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

    suspend fun getMeeting(): MeetingPojo?
    suspend fun setMeeting(value: MeetingPojo)
    suspend fun clearMeeting()

    suspend fun getClassHour(): ClassHourPojo?
    suspend fun setClassHour(value: ClassHourPojo)
    suspend fun clearClassHour()

    suspend fun getEvents(): EventsPojo?
    suspend fun setEvents(value: EventsPojo)
    suspend fun clearEvents()
}