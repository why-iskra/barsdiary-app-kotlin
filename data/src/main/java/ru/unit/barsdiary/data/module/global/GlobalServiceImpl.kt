package ru.unit.barsdiary.data.module.global

import ru.unit.barsdiary.data.transformer.BirthdaysTransformer
import ru.unit.barsdiary.data.transformer.BoxTransformer
import ru.unit.barsdiary.domain.global.GlobalService
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.sdk.BarsDiaryEngine
import javax.inject.Inject

class GlobalServiceImpl @Inject constructor(
    private val engine: BarsDiaryEngine,

    private val birthdaysTransformer: BirthdaysTransformer,
    private val boxTransformer: BoxTransformer,
) : GlobalService {
    override suspend fun getBirthdays(): BirthdaysPojo = birthdaysTransformer.transform(engine.api { getBirthdays() })

    override suspend fun getInBox(page: Int): BoxPojo = boxTransformer.transform(engine.api { getInBoxMessages(page) })

    override suspend fun getOutBox(page: Int): BoxPojo = boxTransformer.transform(engine.api { getOutBoxMessages(page) })

    override suspend fun getInBoxCount(): Int = engine.api { getInBoxCount() }

    override suspend fun removeInBoxMessages(list: List<Int>): Boolean = engine.api { removeInBoxMessages(list.joinToString(",") { it.toString() }) }

    override suspend fun removeOutBoxMessages(list: List<Int>): Boolean =
        engine.api { removeOutBoxMessages(list.joinToString(",") { it.toString() }) }

    override suspend fun markRead(id: Int) {
        engine.api { markRead(id) }
    }
}