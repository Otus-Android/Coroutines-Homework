package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e : Exception) {
        logException(this@CrashMonitor, e.message.toString())
    }
}