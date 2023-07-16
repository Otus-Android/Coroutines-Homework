package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.Dispatchers

/**
 * Реализация обертки получения `coroutine dispatcher` [Dispatcher]
 */
class DispatcherImpl : Dispatcher {

    override val main = Dispatchers.Main
}