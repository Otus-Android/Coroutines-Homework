package otus.homework.coroutines

sealed class Result<T>(open val data: T)

data class Success<T>(override var data: T): Result<T>(data)

data class Error<String>(override val data: String): Result<String>(data)