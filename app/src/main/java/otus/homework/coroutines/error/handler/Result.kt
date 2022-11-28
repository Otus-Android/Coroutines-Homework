package otus.homework.coroutines.error.handler

import otus.homework.coroutines.network.facts.base.CatData

sealed class Result {
    data class Success(
        val catData: CatData
    ) : Result()

    data class Error(
        val t: Throwable?
    ) : Result()
}
