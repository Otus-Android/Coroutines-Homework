package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(throwableMessage: String?) {
        Log.d("CATS_COROUTINE", "trackWarning: $throwableMessage")
    }
}