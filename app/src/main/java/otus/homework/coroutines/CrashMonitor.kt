package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(tag: String, message: String?, stackTrace: String) {
        Log.d(tag, "message: $message, stackTrace: $stackTrace")
    }
}