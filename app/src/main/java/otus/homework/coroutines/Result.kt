package otus.homework.coroutines

sealed interface Result<out T>

data class Success<T>(
    val model: T
) : Result<T>

data class Error(
    val message: String? = null
) : Result<Nothing>