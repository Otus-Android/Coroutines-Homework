package otus.homework.coroutines

import android.os.Bundle

sealed class Result {
    object NoResult: Result()
    class Success(val bundle: Bundle) : Result()
    class Error(val error: String) : Result()
}
