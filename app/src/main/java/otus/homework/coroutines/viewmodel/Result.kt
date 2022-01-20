package otus.homework.coroutines.viewmodel

import androidx.annotation.StringRes

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Error(val msg: String? = null, @StringRes val resId: Int? = null, ) : Result<Nothing>()
}
