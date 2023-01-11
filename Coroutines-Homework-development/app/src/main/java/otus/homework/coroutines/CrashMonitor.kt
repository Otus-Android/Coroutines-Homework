package otus.homework.coroutines

object CrashMonitor {
    const val KEY_LOADING ="key_loading"
    private val crash = mutableMapOf<String,Throwable>()

    /**
     * Pretend this is Crashlytics/AppCenter
     */
    fun trackWarning(key:String, exception: Throwable) {
        crash.put(key,exception)
    }

}