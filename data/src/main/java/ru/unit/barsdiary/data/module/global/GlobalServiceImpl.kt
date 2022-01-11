package ru.unit.barsdiary.data.module.global

import ru.unit.barsdiary.data.transformer.*
import ru.unit.barsdiary.domain.global.GlobalService
import ru.unit.barsdiary.domain.global.pojo.*
import ru.unit.barsdiary.sdk.Constants
import ru.unit.barsdiary.sdk.Engine
import javax.inject.Inject

class GlobalServiceImpl @Inject constructor(
    private val engine: Engine,

    private val birthdaysTransformer: BirthdaysTransformer,
    private val boxTransformer: BoxTransformer,
    private val searchResultTransformer: SearchResultTransformer,
    private val advertBoardTransformer: AdvertBoardTransformer,
    private val meetingTransformer: MeetingTransformer,
    private val eventsTransformer: EventsTransformer,
    private val classHourTransformer: ClassHourTransformer,
) : GlobalService {
    override suspend fun getBirthdays(): BirthdaysPojo = birthdaysTransformer.transform(engine.api { getBirthdays() })

    override suspend fun getInBox(page: Int): BoxPojo = boxTransformer.transform(engine.api { getInBoxMessages(page) })

    override suspend fun getOutBox(page: Int): BoxPojo = boxTransformer.transform(engine.api { getOutBoxMessages(page) })

    override suspend fun getInBoxCount(): Int = engine.api { getInBoxCount() }

    override suspend fun getAdvertBoard(): AdvertBoardPojo = advertBoardTransformer.transform(engine.api { getAdvertBoard() })

    override suspend fun getMeeting(): MeetingPojo = meetingTransformer.transform(engine.api { getMeeting() })

    override suspend fun getClassHour(): ClassHourPojo = classHourTransformer.transform(engine.api { getClassHours() })

    override suspend fun getEvents(): EventsPojo = eventsTransformer.transform(engine.api { getEvents() })

    override suspend fun removeInBoxMessages(list: List<Int>): Boolean = engine.api { removeInBoxMessages(list.joinToString(",") { it.toString() }) }

    override suspend fun removeOutBoxMessages(list: List<Int>): Boolean =
        engine.api { removeOutBoxMessages(list.joinToString(",") { it.toString() }) }

    override suspend fun markRead(id: Int) {
        engine.api { markRead(id) }
    }

    override suspend fun searchUser(type: Int, searchText: String, page: Int): SearchResultPojo = searchResultTransformer.transform(
        engine.api { getUserProfiles(Constants.SEARCH_USER_TYPES[type], page, searchText) }
    )

    override suspend fun sendMessage(receiversIds: String, subject: String, message: String): Boolean {
        return engine.api { newMailBoxMessage(receiversIds, subject, message) }
    }

    override suspend fun sendMessage(
        receiversIds: String,
        subject: String,
        message: String,
        documentNames: String,
        fileNames: String,
        documents: String
    ): Boolean {
        return engine.api { newMailBoxMessage(receiversIds, subject, message, documentNames, fileNames, documents) }
    }
}