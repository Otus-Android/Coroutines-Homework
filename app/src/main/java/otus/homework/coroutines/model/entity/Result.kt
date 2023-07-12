package otus.homework.coroutines.model.entity

sealed class Result<out T> {

    class Success<T>(val item: T) : Result<T>()
    object Error : Result<Nothing>()
}
