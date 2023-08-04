package otus.homework.coroutines.utils

import android.util.Log

/**
 * Утилитное средство мониторинга возникающих ошибок
 */
object CrashMonitor {

    /**
     * Зафиксировать возникшую проблему
     *
     * @param error исключение
     */
    fun trackWarning(error: Throwable) {
        Log.e(TAG, "trackWarning: $error")
    }

    private const val TAG = "CrashMonitor"
}