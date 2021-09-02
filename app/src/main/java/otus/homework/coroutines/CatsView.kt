package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

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
            catsViewModel?.onViewInitializationComplete()
        }
    }

    override fun handleResponse(result: Success) {
        populate(result.catInfo)
    }

    override fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.text
        Picasso.get().load(catInfo.url)
            .into(findViewById<ImageView>(R.id.iv_cat))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showGenericErrorMsg() {
        Toast.makeText(context, context.getString(R.string.generic_error), Toast.LENGTH_LONG).show()

    }

    override fun showSocketExceptionMsg() {
        Toast.makeText(context, context.getString(R.string.exeption_socket_timeout), Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun handleResponse(result: Success)
    fun populate(catInfo: CatInfo)
    fun showToast(message: String)
    fun showSocketExceptionMsg()
    fun showGenericErrorMsg()
}