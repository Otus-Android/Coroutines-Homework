package otus.homework.coroutines.utils.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {

    val main: CoroutineDispatcher
}