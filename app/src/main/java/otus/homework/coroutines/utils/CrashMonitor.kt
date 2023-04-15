package otus.homework.coroutines.utils

import android.util.Log

const val TAG_LOG = "CrashMonitor"
const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    private val crashlyticsErrorList = mutableListOf<String>()
    fun trackWarning(message: String? = UNKNOWN_ERROR) {
        crashlyticsErrorList.add(message ?: UNKNOWN_ERROR)
        Log.e(TAG_LOG, message.toString())
    }
}