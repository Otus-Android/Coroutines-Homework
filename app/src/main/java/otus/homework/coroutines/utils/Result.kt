package otus.homework.coroutines.utils

import otus.homework.coroutines.models.CatsModel

sealed class Result{
    class Success(val catsModel: CatsModel) : Result()
    class Error(val error: Throwable?) : Result()
}
