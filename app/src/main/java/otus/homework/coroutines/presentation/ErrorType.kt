package otus.homework.coroutines.presentation

sealed interface ErrorType {
    class OccurredException(val message: String) : ErrorType
    object ServerConnectionError : ErrorType
    object UnknownError : ErrorType
}