package otus.homework.coroutines


sealed class Result{
    data class Error(val error : String) : Result()
    data class Success<T>(val response : T) : Result()
}
