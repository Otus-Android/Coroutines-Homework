package otus.homework.coroutines

sealed class State<out r> {

    data class success<out t>(val data: t) : State<t>()
    data class error(val exception: Exception) : State<Nothing>()

    companion object {
        inline fun <T> on(f: () -> T): State<T> = try {
            success(f())
        } catch (ex: Exception) {
            error(ex)
        }
    }
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorMsg: String) : Result<Nothing>()
}