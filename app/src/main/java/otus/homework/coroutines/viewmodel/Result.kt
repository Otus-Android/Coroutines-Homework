package otus.homework.coroutines.viewmodel

import otus.homework.coroutines.model.CatData

/**
 * @author Kovrizhkin V. on 08.11.2021
 */
sealed class Result {
    class Error<T>(val errorInfo: T) : Result()
    class Success (val result: CatData) : Result()
}