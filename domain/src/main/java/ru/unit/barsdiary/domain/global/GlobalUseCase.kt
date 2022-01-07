package ru.unit.barsdiary.domain.global

import ru.unit.barsdiary.domain.global.pojo.*
import javax.inject.Inject

interface GlobalUseCase {
    suspend fun getBirthdays(): BirthdaysPojo
    suspend fun getInBox(page: Int): BoxPojo
    suspend fun getOutBox(page: Int): BoxPojo
    suspend fun getInBoxCount(): Int

    suspend fun clearBirthdays()
    suspend fun clearInBox()
    suspend fun clearOutBox()
    suspend fun clearInBoxCount()
    suspend fun clear()

    suspend fun markRead(id: Int)
    suspend fun removeInBoxMessages(list: List<Int>): Boolean
    suspend fun removeOutBoxMessages(list: List<Int>): Boolean

    suspend fun searchUser(type: Int, searchText: String, page: Int): SearchResultPojo

    suspend fun sendMessage(users: List<String>, subject: String, message: String, attachment: MessageAttachmentPojo?): Boolean
}

class GlobalUseCaseImpl @Inject constructor(
    private val globalRepository: GlobalRepository,
    private val globalService: GlobalService,
) : GlobalUseCase {

    override suspend fun getBirthdays(): BirthdaysPojo =
        globalRepository.getBirthdays() ?: globalService.getBirthdays().apply { globalRepository.setBirthdays(this) }

    override suspend fun getInBox(page: Int): BoxPojo =
        globalRepository.getInBox(page) ?: globalService.getInBox(page).apply { globalRepository.setInBox(this, page) }

    override suspend fun getOutBox(page: Int): BoxPojo =
        globalRepository.getOutBox(page) ?: globalService.getOutBox(page).apply { globalRepository.setOutBox(this, page) }

    override suspend fun getInBoxCount(): Int =
        globalService.getInBoxCount()
//        globalRepository.getInBoxCount() ?: globalService.getInBoxCount().apply { globalRepository.setInBoxCount(this) }

    override suspend fun clearBirthdays() {
        globalRepository.clearBirthdays()
    }

    override suspend fun clearInBox() {
        globalRepository.clearInBox()
    }

    override suspend fun clearOutBox() {
        globalRepository.clearOutBox()
    }

    override suspend fun clearInBoxCount() {
        globalRepository.clearInBoxCount()
    }

    override suspend fun clear() {
        clearBirthdays()
        clearInBox()
        clearOutBox()
        clearInBoxCount()
    }

    override suspend fun markRead(id: Int) {
        globalService.markRead(id)
    }

    override suspend fun removeInBoxMessages(list: List<Int>): Boolean {
        val result = globalService.removeInBoxMessages(list)
        clearInBox()

        return result
    }

    override suspend fun removeOutBoxMessages(list: List<Int>): Boolean {
        val result = globalService.removeOutBoxMessages(list)
        clearOutBox()

        return result
    }

    override suspend fun searchUser(type: Int, searchText: String, page: Int) = globalService.searchUser(type, searchText, page)

    override suspend fun sendMessage(users: List<String>, subject: String, message: String, attachment: MessageAttachmentPojo?): Boolean {
        val ids = users.joinToString(separator = ",")
        return if(attachment == null) {
            globalService.sendMessage(ids, subject, message)
        } else {
            globalService.sendMessage(ids, subject, message, attachment.name, "[\"${attachment.name}\"]", attachment.data)
        }
    }
}