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

    var presenter: CatsPresenter? = null
    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            catsViewModel?.onInitComplete()
        }
    }

    override fun handleResponse(result: Result) {
        when (result) {
            is Success -> {
                populate(result.catInfo)
            }
            is Error -> {
                showToastByException(result.ex)
            }
        }
    }

    override fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.text
        Picasso.get().load(catInfo.url)
            .into(findViewById<ImageView>(R.id.iv_cat))
    }

    override fun showToastByException(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException -> {
                showToast(context.getString(R.string.exeption_socket_timeout))
            }
            else -> {
                showToast(ex.message ?: context.getString(R.string.generic_error))
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun handleResponse(result: Result)
    fun populate(catInfo: CatInfo)
    fun showToastByException(ex: Throwable)
    fun showToast(message: String)
}