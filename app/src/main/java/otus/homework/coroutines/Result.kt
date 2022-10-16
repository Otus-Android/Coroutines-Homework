package otus.homework.coroutines

sealed class Result {
    class Success<T> : Result() {
        var successBody: T? = null
    }

    class Error(
        val message: String
    ) : Result()

}
