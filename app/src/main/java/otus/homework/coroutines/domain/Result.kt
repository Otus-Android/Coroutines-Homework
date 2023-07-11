package otus.homework.coroutines.domain

sealed interface Result<T : Any?>

class Success<T : Any>(val data: T) : Result<T>

class Error<T : Any>(val errorCode: Int?, val errorMessage: String?) : Result<T>

class ResultException<T : Any>(val exceptionMessage: String?) : Result<T>



