package otus.homework.coroutines

import androidx.annotation.StringRes

sealed class Result {
    class Success(val factWithPicture: FactWithPicture): Result()
    class Error(val message: String? = null, @StringRes val messageRes: Int? = null): Result()
}
