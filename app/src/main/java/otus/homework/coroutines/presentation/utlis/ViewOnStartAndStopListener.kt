package otus.homework.coroutines.presentation.utlis

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner

class ViewOnStartAndStopListener(
    private val owner: LifecycleOwner,
    private val onStartListener: () -> Unit,
    private val onStopListener: () -> Unit,
) : LifecycleEventObserver {

    init {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> onStartListener()
            Lifecycle.Event.ON_STOP -> onStopListener()
            Lifecycle.Event.ON_DESTROY -> owner.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}

fun View.setupOnStopListener(onStartListener: () -> Unit, onStopListener: () -> Unit) {
    findViewTreeLifecycleOwner()?.let { owner ->
        ViewOnStartAndStopListener(owner, onStartListener, onStopListener)
    }
}