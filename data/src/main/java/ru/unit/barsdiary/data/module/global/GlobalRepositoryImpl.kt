package ru.unit.barsdiary.data.module.global

import ru.unit.barsdiary.data.di.annotation.BirthdaysRamCache
import ru.unit.barsdiary.data.di.annotation.InBoxCountRamCache
import ru.unit.barsdiary.data.di.annotation.InBoxRamCache
import ru.unit.barsdiary.data.di.annotation.OutBoxRamCache
import ru.unit.barsdiary.data.utils.RamCache
import ru.unit.barsdiary.domain.global.GlobalRepository
import ru.unit.barsdiary.domain.global.pojo.BirthdaysPojo
import ru.unit.barsdiary.domain.global.pojo.BoxPojo
import javax.inject.Inject

class GlobalRepositoryImpl @Inject constructor(
    @BirthdaysRamCache private val birthdaysCache: RamCache<BirthdaysPojo>,
    @InBoxRamCache private val inBoxCache: RamCache<BoxPojo>,
    @OutBoxRamCache private val outBoxCache: RamCache<BoxPojo>,
    @InBoxCountRamCache private val inBoxCountCache: RamCache<Int>,
) : GlobalRepository {

    override suspend fun getBirthdays(): BirthdaysPojo? = birthdaysCache.get()

    override suspend fun setBirthdays(value: BirthdaysPojo) {
        birthdaysCache.put(value)
    }

    override suspend fun clearBirthdays() {
        birthdaysCache.clear()
    }

    override suspend fun getInBox(page: Int): BoxPojo? = inBoxCache.get(page)

    override suspend fun setInBox(value: BoxPojo, page: Int) {
        inBoxCache.put(value, page)
    }

    override suspend fun clearInBox() {
        inBoxCache.clear()
    }

    override suspend fun getOutBox(page: Int): BoxPojo? = outBoxCache.get(page)

    override suspend fun setOutBox(value: BoxPojo, page: Int) {
        outBoxCache.put(value, page)
    }

    override suspend fun clearOutBox() {
        outBoxCache.clear()
    }

    override suspend fun getInBoxCount(): Int? = inBoxCountCache.get()

    override suspend fun setInBoxCount(value: Int) {
        inBoxCountCache.put(value)
    }

    override suspend fun clearInBoxCount() {
        inBoxCountCache.clear()
    }
}