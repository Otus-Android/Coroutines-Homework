package otus.homework.coroutines.domain

import android.util.Log

object CrashMonitor {
	var tag = "CrashMonitor"

	/**
	 * Pretend this is Crashlytics/AppCenter
	 */
	fun trackWarning(t: Throwable) {
		Log.w(tag, t)
	}
}