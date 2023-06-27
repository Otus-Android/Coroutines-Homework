package otus.homework.coroutines.view

import otus.homework.coroutines.model.CatModel

sealed class Result{
    data class Success(val data: CatModel) : Result()
    data class Error(val error : Throwable) : Result()
}
