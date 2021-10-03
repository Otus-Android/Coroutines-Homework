package otus.homework.coroutines.utils

/**
 * @author Dmitry Kartsev (Jag)
 * @sinse 21.06.2021
 */

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * @author Dmitry Kartsev (Jag)
 * @sinse 14.06.2021
 */

fun <T> Flow<T>.launchWhenCreated(coroutineScope: LifecycleCoroutineScope) {
    coroutineScope.launchWhenCreated {
        this@launchWhenCreated.collect()
    }
}

fun <T> Flow<T>.launchWhenStarted(coroutineScope: LifecycleCoroutineScope) {
    coroutineScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.launchWhenResumed(coroutineScope: LifecycleCoroutineScope) {
    coroutineScope.launchWhenResumed {
        this@launchWhenResumed.collect()
    }
}