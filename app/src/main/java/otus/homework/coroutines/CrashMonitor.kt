package otus.homework.coroutines

object CrashMonitor {
    private const val TAG = "CrashMonitor"

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(t: Throwable) {
        t.printStackTrace()
    }
}