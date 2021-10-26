package otus.homework.coroutines

import android.util.Log
import java.lang.Exception

object CrashMonitor {

    private const val TAG = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning() {
    }

    fun logException(e: Exception) {
        Log.d(TAG, "Exception: $e")
    }

    fun logThrowable(throwable: Throwable) {
        Log.d(TAG, "Throwable: $throwable")
    }
}