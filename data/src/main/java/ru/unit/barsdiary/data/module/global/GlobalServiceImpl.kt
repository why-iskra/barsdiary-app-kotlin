package ru.unit.barsdiary.data.module.global

import ru.unit.barsdiary.data.transformer.BirthdaysTransformer
import ru.unit.barsdiary.data.transformer.BoxTransformer
import ru.unit.barsdiary.domain.global.GlobalService
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import ru.unit.barsdiary.sdk.BarsWrapper
import javax.inject.Inject

class GlobalServiceImpl @Inject constructor(
    private val barsWrapper: BarsWrapper,
    private val birthdaysTransformer: BirthdaysTransformer,
    private val boxTransformer: BoxTransformer,
) : GlobalService {
    override suspend fun getBirthdays(): BirthdaysPojo = birthdaysTransformer.transform(barsWrapper.getBirthdays())

    override suspend fun getInBox(page: Int): BoxPojo = boxTransformer.transform(barsWrapper.getInBoxMessages(page))

    override suspend fun getOutBox(page: Int): BoxPojo = boxTransformer.transform(barsWrapper.getOutBoxMessages(page))

    override suspend fun getInBoxCount(): Int = barsWrapper.getInBoxCount()

    override suspend fun markRead(id: Int) {
        barsWrapper.markRead(id)
    }
}