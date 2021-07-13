package otus.homework.coroutines.entity

import otus.homework.coroutines.ErrorState

sealed class Result {

    data class Success(
        val animal: Animal
    ) : Result()

    class Error(
        val errorState: ErrorState
    ) : Result()
}