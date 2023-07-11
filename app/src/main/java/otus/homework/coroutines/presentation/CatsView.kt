package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.R.string
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.Result.Error
import otus.homework.coroutines.utils.Result.Success
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private val viewModel = CatsViewModel()
    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
            populate()
        }
    }

    override fun populate() {
        when (viewModel.result.value) {
            is Success -> {
                val resultSuccess = (viewModel.result.value as? Success)
                findViewById<TextView>(R.id.fact_textView).text = resultSuccess?.catsModel?.fact?.fact
                if (resultSuccess?.catsModel?.catsImage?.isNotEmpty() == true) {
                    Picasso.get()
                        .load(resultSuccess.catsModel.catsImage.random().url)
                        .into(findViewById<ImageView>(R.id.imageView))
                }
            }

            else -> {
                showToastException(exceptions = (viewModel.result.value as? Error)?.error, context = context)
            }
        }
    }

    private fun showToastException(exceptions: Throwable?, context: Context) {
        val message = if (exceptions is SocketTimeoutException) {
            context.getString(string.socket_timeout_exception)
        } else {
            val exceptionMessage = exceptions
                ?.toString()
                ?.substringAfter(delimiter = context.getString(string.substring_for_exception))
                .orEmpty()
            CrashMonitor.trackWarning(exceptionMessage = exceptionMessage)
            exceptionMessage
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
