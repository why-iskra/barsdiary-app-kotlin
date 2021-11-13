package ru.unit.barsdiary.other

import android.text.Html
import android.text.Spanned
import javax.inject.Inject

object HtmlUtils {

    const val nonBreakWhitespaceSign = "&nbsp;"
    const val lessThanSign = "&lt;"
    const val greaterThanSign = "&gt;"
    const val ampersandSign = "&amp;"

    const val tagAHref = "<a href=\"%s\">%s</a>"
    const val tagNewLine = "<br/>"

    private val replaceMap = mapOf(
        "&" to ampersandSign,
        "<" to lessThanSign,
        ">" to greaterThanSign
    )

    fun hrefDocument(name: String, url: String): String = tagAHref.format(url, name)

    fun prepareText(text: String): String {
        var result = text
        replaceMap.forEach { (old, new) ->
            result = result.replace(old, new)
        }

        return result
    }

    fun convert(text: String?): Spanned? = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
}
