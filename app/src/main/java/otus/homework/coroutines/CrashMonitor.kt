package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning() = Unit
    fun trackException(e: Throwable) = println("Log error: ${e.message}")
}