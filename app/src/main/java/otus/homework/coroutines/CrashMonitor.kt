package otus.homework.coroutines

object CrashMonitor {

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(e : Throwable) {
        logException(this@CrashMonitor, e.message.toString())
    }
}