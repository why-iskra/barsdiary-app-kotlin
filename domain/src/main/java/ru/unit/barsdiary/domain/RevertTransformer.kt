package ru.unit.barsdiary.domain

interface RevertTransformer<T, R> : BaseTransformer<T, R> {
    fun revert(value: R): T
}