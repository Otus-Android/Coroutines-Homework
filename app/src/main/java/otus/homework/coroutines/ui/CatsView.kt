package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.CatsState
import otus.homework.coroutines.presentation.ErrorType

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(state: CatsState) {
        findViewById<TextView>(R.id.fact_textView).text = state.fact?.text ?: ""
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso.get().load(state.image?.url).into(imageView)
    }

    override fun showError(error: ErrorType) {
        val message = when (error) {
            ErrorType.ServerConnectionError -> context.getString(R.string.connection_error)
            is ErrorType.OccurredException -> error.message
            ErrorType.UnknownError -> return
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {
    fun populate(state: CatsState)
    fun showError(error: ErrorType)

}