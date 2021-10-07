package otus.homework.coroutines.utils

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


inline fun CoroutineScope.createExceptionHandler(
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    launch {
        action(throwable)
    }
}