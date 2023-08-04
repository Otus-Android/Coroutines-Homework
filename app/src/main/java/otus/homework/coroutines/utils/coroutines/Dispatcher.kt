package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Обертка получения `coroutine dispatcher`
 */
interface Dispatcher {

    /** `main coroutine dispatcher` */
    val main: CoroutineDispatcher

    /** `main immediate coroutine dispatcher` */
    val mainImmediate: CoroutineDispatcher
}