package ru.unit.barsdiary.other

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppLog @Inject constructor() {

    private val logList = mutableListOf<String>()
    private val updateFlow = MutableSharedFlow<Unit>()
    private val mutex = Mutex()

    val publicLogList: List<String> get() = logList
    val publicUpdateFlow: SharedFlow<Unit> get() = updateFlow

    fun println(priorityInt: Int, tag: String?, raw: String) {
        val text = HtmlUtils.prepareText(raw)

        val prefix = priorityToString(priorityInt)
        val prefixText = prefix.first + " " + getCurrentTime()
        val prefixColor = prefix.second

        val list = text.split("\n")

        if (list.size < 2) {
            addLine("<span white-space=\"pre\"><font color='${prefixColor}'>${prefixText} $tag > ${list[0]}</font></span>")
        } else {
            val size = prefixText.length
            val spacePrefix = buildString {
                for (i in 0 until size) {
                    append(HtmlUtils.nonBreakWhitespaceSign)
                }
            }

            for (i in list.indices) {
                when (i) {
                    0 -> {
                        addLine("<font color='${prefixColor}'>${prefixText} + ${list[i]}</font>")
                    }
                    list.size - 1 -> {
                        addLine("<font color='${prefixColor}'>${spacePrefix} + ${list[i]}</font>")
                    }
                    else -> {
                        addLine("<font color='${prefixColor}'>${spacePrefix} | ${list[i]}</font>")
                    }
                }
            }
        }
    }

    fun wtf(tag: String?, text: String) {
        println(WTF, tag, text)
    }

    private fun addLine(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                while (logList.size > MAX_BUFFER_LINES) {
                    logList.removeAt(0)
                }

                logList.add(text)
            }
            updateFlow.emit(Unit)
        }
    }

    private fun priorityToString(priority: Int) = when (priority) {
        VERBOSE -> "[V]" to "grey"
        DEBUG -> "[D]" to "grey"
        INFO -> "[I]" to "grey"
        WARN -> "[W]" to "#FF8C00"
        ERROR -> "[E]" to "#DC143C"
        ASSERT -> "[A]" to "#9932CC"
        WTF -> "[wtf]" to "#DC143C"
        else -> "[?]" to "#DCDCDC"
    }

    private fun getCurrentTime() = LocalDateTime.now().format(PREFIX_DATE)

    companion object {
        // Decompile from android
        private const val VERBOSE = 2
        private const val DEBUG = 3
        private const val INFO = 4
        private const val WARN = 5
        private const val ERROR = 6
        private const val ASSERT = 7

        private const val WTF = 8


        private val PREFIX_DATE = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

        private const val MAX_BUFFER_LINES = 2000
    }

}