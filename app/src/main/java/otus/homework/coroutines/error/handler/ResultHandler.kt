package otus.homework.coroutines.error.handler

import android.content.Context
import android.widget.Toast
import otus.homework.coroutines.R
import java.net.SocketTimeoutException

class ResultHandler(private val context: Context) : ResultEvent {

    override fun onResult(result: Result) {
        when (result) {
            is Result.Success -> onSuccess(result.id)
            is Result.Error -> onError(result.t)
        }
    }

    private fun onSuccess(id: Int?) {
        id?.let { showToast(context.getString(it)) }
    }

    private fun onError(throwable: Throwable?) {
        val errorText = context.getString(
            if (throwable is SocketTimeoutException) {
                R.string.socket_timeout_exception_error_text
            } else {
                R.string.unknown_error_text
            }
        )
        showToast(errorText)
    }


    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}