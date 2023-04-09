package com.example.welcometothemooncompanion.util

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Analog of [LiveData.observe] method
 */
inline fun <T> Flow<T>.observe(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        collect { onCollect(it) }
    }
}
