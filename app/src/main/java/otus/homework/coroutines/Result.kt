package otus.homework.coroutines

sealed class Result {
    data class Success(val item: CatViewItem) : Result()
    data class Error(val message: String) : Result()
}

