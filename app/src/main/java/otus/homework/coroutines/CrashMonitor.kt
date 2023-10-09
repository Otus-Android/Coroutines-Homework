package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(tr: Throwable) {
        Log.e("LOG_CATS", tr.message, tr)
    }
}