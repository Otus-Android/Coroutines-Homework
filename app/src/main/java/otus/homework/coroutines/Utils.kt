package otus.homework.coroutines

import android.util.Log

fun logException(obj: Any, e: String) {
    Log.w(obj.javaClass.simpleName, e)
}