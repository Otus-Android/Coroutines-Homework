package otus.homework.coroutines

import otus.homework.coroutines.models.CatFactPic

sealed class Result {
    data class Success(val value: CatFactPic) : Result()
    data class Error(val message:String) : Result()
}
