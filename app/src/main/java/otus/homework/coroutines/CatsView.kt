package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.error.handler.Result
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    fun setUpButtonCallback(getFactAndImage: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            getFactAndImage()
        }
    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Success -> {
                findViewById<TextView>(R.id.fact_textView).text = result.catData.fact.text
                val imageView = findViewById<ImageView>(R.id.fact_image)
                Picasso.get().load(result.catData.imageUrl.file).into(imageView)
            }
            is Result.Error -> {
                when (result.t) {
                    is SocketTimeoutException -> showToast(context.getString(R.string.socket_timeout_exception_error_text))
                    else -> showToast(
                        result.t?.message ?: context.getString(R.string.unknown_error_text)
                    )
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(result: Result)
}