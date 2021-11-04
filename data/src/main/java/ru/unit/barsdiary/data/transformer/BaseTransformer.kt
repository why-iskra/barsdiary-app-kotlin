package ru.unit.barsdiary.data.transformer

interface BaseTransformer<T, R> {
    fun transform(value: T): R
}