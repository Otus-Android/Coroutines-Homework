package otus.homework.coroutines.model

/**
 * Обертка для обработки состояний (Загрузка, Успех, Ошибка).
 * Реализовано согласно пункту задания о создании sealed-класса Result.
 */
sealed class Result<out T>

/** Состояние в процессе получения данных */
object Loading : Result<Nothing>()

/** Успешное получение данных */
data class Success<T>(val data: T) : Result<T>()

/** Ошибка при получении данных */
data class Error(val message: String) : Result<Nothing>()
