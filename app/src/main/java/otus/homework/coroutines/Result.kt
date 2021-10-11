package otus.homework.coroutines

sealed class Result<out T>

/**
 * Результат с успешным стейтом
 *
 * @param state стейт успешного результата
 */
data class Success<T>(val state: T) : Result<T>()

/**
 * Результат с ошибкой
 *
 * @param message сообщение с ошибкой
 */
data class Error(val message: String) : Result<Nothing>()
