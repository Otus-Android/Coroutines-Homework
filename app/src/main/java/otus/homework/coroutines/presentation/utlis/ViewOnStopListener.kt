package otus.homework.coroutines.presentation.utlis

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner

class ViewOnStopListener(
    private val owner: LifecycleOwner,
    private val listener: () -> Unit,
) : LifecycleEventObserver {

    init {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> listener()
            Lifecycle.Event.ON_DESTROY -> owner.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}

fun View.setupOnStopListener(listener: () -> Unit) {
    findViewTreeLifecycleOwner()?.let { owner ->
        ViewOnStopListener(owner, listener)
    }
}