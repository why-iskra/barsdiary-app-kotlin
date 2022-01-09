package ru.unit.barsdiary.domain.global

import ru.unit.barsdiary.domain.global.pojo.AdvertBoardPojo
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.domain.global.pojo.SearchResultPojo

interface GlobalService {
    suspend fun getBirthdays(): BirthdaysPojo
    suspend fun getInBox(page: Int): BoxPojo
    suspend fun getOutBox(page: Int): BoxPojo
    suspend fun getInBoxCount(): Int
    suspend fun getAdvertBoard(): AdvertBoardPojo

    suspend fun removeInBoxMessages(list: List<Int>): Boolean
    suspend fun removeOutBoxMessages(list: List<Int>): Boolean
    suspend fun markRead(id: Int)
    suspend fun searchUser(type: Int, searchText: String, page: Int): SearchResultPojo

    suspend fun sendMessage(receiversIds: String, subject: String, message: String): Boolean
    suspend fun sendMessage(receiversIds: String, subject: String, message: String, documentNames: String, fileNames: String, documents: String): Boolean
}