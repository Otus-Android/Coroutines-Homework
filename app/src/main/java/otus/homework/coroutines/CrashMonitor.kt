package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(exception: java.lang.Exception) {
        Log.w("CrashMonitor", "Cause: ", exception.cause)
    }
}