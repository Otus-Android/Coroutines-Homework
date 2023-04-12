package otus.homework.coroutines

sealed class Result
class Success(val catData: CatData) : Result()
class Error(val errorMassage: String, val isSocketTimeoutException: Boolean) : Result()

