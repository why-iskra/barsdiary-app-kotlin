package ru.unit.barsdiary.other

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppLog @Inject constructor() {

    private val logList = mutableListOf<String>()
    private val updateFlow = MutableSharedFlow<Unit>()

    val publicLogList: List<String> get() = logList
    val publicUpdateFlow: SharedFlow<Unit> get() = updateFlow

    fun println(priorityInt: Int, tag: String?, text: String) {
        val priority = priorityToString(priorityInt)

        val list = text.split("\n")

        if (list.size < 2) {
            addLine("<span white-space=\"pre\"><font color='${priority.second}'>${priority.first} $tag > ${list[0]}</font></span>")
        } else {
            val size = priority.first.length
            val spacePrefix = buildString {
                for (i in 0 until size) {
                    append("&nbsp;")
                }
            }

            for (i in list.indices) {
                when (i) {
                    0 -> {
                        addLine("<font color='${priority.second}'>${priority.first} + ${list[i]}</font>")
                    }
                    list.size - 1 -> {
                        addLine("<font color='${priority.second}'>${spacePrefix} + ${list[i]}</font>")
                    }
                    else -> {
                        addLine("<font color='${priority.second}'>${spacePrefix} | ${list[i]}</font>")
                    }
                }
            }
        }
    }

    fun wtf(tag: String?, text: String) {
        println(WTF, tag, text)
    }

    private fun addLine(text: String) {
        logList.add(text)
        CoroutineScope(Dispatchers.Main).launch {
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
        val VERBOSE = 2
        val DEBUG = 3
        val INFO = 4
        val WARN = 5
        val ERROR = 6
        val ASSERT = 7
        val WTF = 8

        private val PREFIX_DATE = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    }

}