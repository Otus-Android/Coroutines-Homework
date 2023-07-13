package otus.homework.coroutines

import otus.homework.coroutines.model.CatModel

sealed class Result {
    data class Success(val catData: CatModel) : Result()
    data class Error(val throwable: Throwable) : Result()
}
