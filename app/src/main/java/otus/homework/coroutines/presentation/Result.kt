package otus.homework.coroutines.presentation

sealed interface Result {
    class Success<T>(data: T)
    class Error(message: String)
}