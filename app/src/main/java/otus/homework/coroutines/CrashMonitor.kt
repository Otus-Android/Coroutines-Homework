package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(tag: String, t: Throwable) {
        Log.e(tag, t.message ?: "Unknown error")
    }
}
