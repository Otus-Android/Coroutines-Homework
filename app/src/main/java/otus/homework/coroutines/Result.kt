package otus.homework.coroutines

sealed class Result<out R> {

    class Success<out T>(val data: T) : Result<T>()

    object Error : Result<Nothing>()
}
