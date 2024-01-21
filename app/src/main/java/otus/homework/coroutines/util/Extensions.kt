package otus.homework.coroutines.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive

inline fun <T : CoroutineScope, R> T.runCatching(block: T.() -> R): Result<R> {
    return kotlin.runCatching { block() }.onFailure { ensureActive() }
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.dangerCast(): T = this as T