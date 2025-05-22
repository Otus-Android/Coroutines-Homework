package otus.homework.coroutines

import androidx.annotation.StringRes

sealed interface CatsScreenState {
    data class Success<T>(val data: T) : CatsScreenState
    data class Error(@StringRes val message: Int, val th: Throwable) : CatsScreenState
}