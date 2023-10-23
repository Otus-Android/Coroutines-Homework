package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.net.SocketTimeoutException

object CrashMonitor {

    private const val SOCKET_TIME_EXCEPTION_ANSWER = "Не удалось получить ответ от сервера."

    fun trackWarning(context: Context, e: Throwable) {
        when(e) {
            is SocketTimeoutException -> {
                Toast.makeText(context, SOCKET_TIME_EXCEPTION_ANSWER, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.d(this.toString(), e.toString())
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}