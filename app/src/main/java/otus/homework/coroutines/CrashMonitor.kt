package otus.homework.coroutines

import android.util.Log

class CrashMonitorImpl(
    private val tag: String
) : CrashMonitor {

    override fun trackInfo(message: String) {
        Log.i(tag, message)
    }

    override fun trackWarning(message: String) {
        Log.w(tag, message)
    }

    override fun trackError(message: String?, error: Throwable) {
        Log.e(tag, message, error)
    }
}

class CrashMonitorEmpty : CrashMonitor {
    override fun trackInfo(message: String) = Unit
    override fun trackWarning(message: String) = Unit
    override fun trackError(message: String?, error: Throwable) = Unit
}

interface CrashMonitor {
    fun trackInfo(message: String)
    fun trackWarning(message: String)
    fun trackError(message: String?, error: Throwable)
}