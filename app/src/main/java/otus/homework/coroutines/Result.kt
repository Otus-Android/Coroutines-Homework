package otus.homework.coroutines

sealed class Result {
    object Loading: Result()
    object Error: Result()
    object Success: Result()
}
