package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: ICatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.updateFactAndImage()
        }
    }

    override fun populate(result: Result<UiState>) {
        when (result) {
            is Result.Success<UiState> -> {
                findViewById<TextView>(R.id.fact_textView).text = result.value.fact
                findViewById<ImageView>(R.id.imageView).also { view ->
                    Picasso.get()
                        .load(result.value.imageUrl)
                        .into(view)
                }
            }

            is Result.Error<*> -> {
                val throwable = result.throwable
                if (throwable is SocketTimeoutException) {
                    showToast(context.getString(R.string.cannot_receive_http_response))
                } else {
                    throwable.message?.let {
                        showToast(it)
                    }
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(result: Result<UiState>)
}