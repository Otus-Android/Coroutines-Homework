package otus.homework.coroutines.presentation

import android.util.Log

object CrashMonitor {

    fun trackWarning(throwable: Throwable) {
        Log.e("MY_LOG", "trackWarning: ${throwable.message}", throwable)
    }
}