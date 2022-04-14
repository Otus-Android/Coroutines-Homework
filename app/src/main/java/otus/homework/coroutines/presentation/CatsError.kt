package otus.homework.coroutines.presentation

sealed class CatsError(val message: String) {
    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Что-то пошло не так"
    }

    object NetworkError : CatsError("Не удалось получить ответ от сервером")
    data class DefaultError(private val exceptionMessage: String?) :
        CatsError(exceptionMessage ?: DEFAULT_ERROR_MESSAGE)
}