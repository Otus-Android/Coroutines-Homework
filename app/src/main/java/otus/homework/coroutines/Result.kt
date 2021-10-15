package otus.homework.coroutines

sealed class Result {
    data class Success< T , B >(val data: T, val data2:B) : Result()
    data class Error(val exception: Exception) : Result()
}