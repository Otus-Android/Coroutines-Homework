package otus.homework.coroutines

sealed class Result<out T>
class Success<out T>(val result: T) : Result<T>()

sealed class Error : Result<Nothing>()
object ServerError : Error()
class OtherError(val errorMessage: String?) : Error()