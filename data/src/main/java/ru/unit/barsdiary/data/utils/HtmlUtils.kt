package ru.unit.barsdiary.data.utils

fun aTagDocument(name: String, url: String): String = "<a href=\"%s\">%s</a>".format(url, name)