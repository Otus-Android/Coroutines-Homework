package otus.homework.coroutines.model

import android.graphics.Bitmap

sealed class Result {
    class Success<T>(val result: T) : Result()

    class Error(val reason: String) : Result()
}