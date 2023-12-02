package otus.homework.coroutines.domain

sealed class Result {
    class Error(val exception: Throwable): Result()
    class Success(val catModel: CatModel): Result()
}
