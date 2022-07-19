package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(message: String?, throwable: Throwable? = null) {
        Log.e("AppException", message, throwable)
    }
}