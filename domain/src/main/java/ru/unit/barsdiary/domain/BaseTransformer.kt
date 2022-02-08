package ru.unit.barsdiary.domain

interface BaseTransformer<T, R> {
    fun transform(value: T): R
}