package otus.homework.coroutines


sealed interface Result

class Success<T>(val data: T):Result
class Error(val message: String): Result