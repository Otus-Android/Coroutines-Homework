package otus.homework.coroutines.presentation.view

sealed class Result<out T>

data class Success<T>(val value : T): Result<T>()
data class Error(val msgId: Int? = null, val msg: String? = null): Result<Nothing>()