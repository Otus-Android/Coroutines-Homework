package otus.homework.coroutines.viewmodel_variant

sealed class Result<out T>
class Error(val message: String?): Result<Nothing>()
class Success<T>(val data: T): Result<T>()



