package otus.homework.coroutines

sealed class DisplayError {

    object Timeout: DisplayError()
    class Other(val message: String): DisplayError()

}
