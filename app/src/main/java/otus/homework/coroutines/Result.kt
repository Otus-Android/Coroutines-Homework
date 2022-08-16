package otus.homework.coroutines

import otus.homework.coroutines.models.Content

sealed interface Result {
    data class Success(val content: Content) : Result
    data class Error(val throwable: Throwable) : Result
}
