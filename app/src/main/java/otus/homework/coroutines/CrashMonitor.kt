package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(throwable: Throwable) {
        val errorMessage = throwable.message ?: throwable.localizedMessage ?: "Error"
        Log.e("CRASH_MONITOR", errorMessage)
    }
}