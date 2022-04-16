package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(th: Throwable) {
        Log.e("CrashMonitor", th.toString())
    }
}