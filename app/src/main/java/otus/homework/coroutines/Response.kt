package otus.homework.coroutines

sealed class Result<out L, out R>

data class Success<out R>(val value: R) : Result<Nothing, R>()
data class Error<out L>(val value: L) : Result<L, Nothing>()