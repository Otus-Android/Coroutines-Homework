package otus.homework.coroutines

sealed class ErrorState {

    object SocketError : ErrorState()
    data class OtherError(val message: String?) : ErrorState()
}