package otus.homework.coroutines

import android.util.Log

object CrashMonitor {
    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(TAG: String, e: Throwable) {
        val message = e.message ?: "Неизвестная ошибка в CatsViewModel"
        Log.e(TAG,message)
    }
}