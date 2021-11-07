package ru.unit.barsdiary.other

fun aTagDocument(name: String, url: String): String = "<a href=\"%s\">%s</a>".format(url, name)