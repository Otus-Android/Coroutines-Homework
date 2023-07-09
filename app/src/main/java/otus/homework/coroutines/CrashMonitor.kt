package otus.homework.coroutines

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    private val crashesList = ArrayList<String>()

    fun trackWarning(message: String) {
        crashesList.add(message)
        Log.e("crash_mirror", message)
    }
}