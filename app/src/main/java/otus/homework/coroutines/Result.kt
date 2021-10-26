package otus.homework.coroutines

sealed class Result

class Success<T>(val data: T): Result()
class Error(val error: String) : Result()
object NetworkError : Result()

