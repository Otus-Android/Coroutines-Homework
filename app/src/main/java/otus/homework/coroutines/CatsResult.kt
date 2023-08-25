package otus.homework.coroutines

sealed interface CatsResult {
    class Success<T> (val result: T): CatsResult
    class Error(val exc: String): CatsResult
}