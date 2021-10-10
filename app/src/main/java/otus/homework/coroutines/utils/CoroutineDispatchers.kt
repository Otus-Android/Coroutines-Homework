package otus.homework.coroutines.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
    val computationDispatcher: CoroutineDispatcher
}