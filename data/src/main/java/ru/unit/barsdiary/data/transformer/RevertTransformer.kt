package ru.unit.barsdiary.data.transformer

interface RevertTransformer<T, R> : BaseTransformer<T, R> {
    fun revert(value: R): T
}