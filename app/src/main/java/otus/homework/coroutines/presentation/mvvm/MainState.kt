package otus.homework.coroutines.presentation.mvvm

sealed class MainState<out T> {

    data class Success<T>(val item: T) : MainState<T>()

    object Error : MainState<Nothing>()
}
