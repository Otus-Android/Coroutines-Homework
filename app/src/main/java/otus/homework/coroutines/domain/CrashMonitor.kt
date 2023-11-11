package otus.homework.coroutines.domain

import android.util.Log

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(warningMessage: String) {
        Log.d("TAG", "Crash logged: $warningMessage")
    }
}