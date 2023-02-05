package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    private val crashlyticsPool = mutableListOf<String?>()
    fun trackWarning(message: String? = "") {
        crashlyticsPool.add(message)
        Log.i("CrashMonitor", message.toString())
    }
}