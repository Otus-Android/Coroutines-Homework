package otus.homework.coroutines

sealed class Result<out T: Any?> {
    data class Success<out T: Any?>(val data: T): Result<T>()
    data class Error(val msg: String?, val cause: Exception? = null): Result<Nothing>()
}

fun <T: Any?, R: Any?> concatResults(finalResultCreation: (succeededResults: List<T>) -> R, results: List<Result<T>>): Result<R> {
    val error = results.find { it is Result.Error }
    return if (error != null && error is Result.Error) {
        error
    } else {
        val succeededResults = results.filterIsInstance<Result.Success<T>>().map { it.data }
        Result.Success<R>(
            finalResultCreation(succeededResults)
        )
    }
}
