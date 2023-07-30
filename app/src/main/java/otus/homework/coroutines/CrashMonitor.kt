package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(t: Throwable) {
        Log.e("Error", t.message ?: "Unknown error")
    }

}