package otus.homework.coroutines.utils

sealed class ResultCats <out T> {
    data class Success<out T>(val value: T) : ResultCats<T>()
    data class Error(val exception: Exception) : ResultCats<Nothing>()
}