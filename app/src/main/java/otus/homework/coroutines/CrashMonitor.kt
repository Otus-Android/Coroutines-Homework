package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(w: Throwable) {
        Log.w("CrashMonitor", w)
    }
}