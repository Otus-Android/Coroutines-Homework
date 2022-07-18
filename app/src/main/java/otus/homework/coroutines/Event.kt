package otus.homework.coroutines

open class Event<out T>(private val data: T) {

    private var hasBeenHandled = false

    fun transferIfNotHandled(): T? = if (hasBeenHandled) null
    else {
        hasBeenHandled = true
        data
    }
}