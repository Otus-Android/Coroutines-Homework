package otus.homework.coroutines

sealed class Result<out T>

class Success<T>(val value: T) : Result<T>()
class Error(val message: String) : Result<Nothing>()