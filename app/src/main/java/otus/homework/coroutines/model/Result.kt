package otus.homework.coroutines.model

import android.graphics.Bitmap

sealed class Result {
    abstract class Success<T>() : Result()

    class SuccessFact(val fact: Fact) : Success<Fact>() {
    }

    class SuccessImage(val image: Bitmap) : Success<Bitmap>() {
    }

    class Error(val reason: String) : Result() {
    }
}