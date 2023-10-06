package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import otus.homework.coroutines.R
import otus.homework.coroutines.di.picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private var refreshButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var textView: TextView? = null
    private var photo: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupChildren()
        refreshButton?.setOnClickListener { presenter?.onInitComplete() }
    }

    private fun setupChildren() {
        refreshButton = findViewById(R.id.button)
        textView = findViewById(R.id.fact_textView)
        progressBar = findViewById(R.id.loading_view)
        photo = findViewById(R.id.photo)
    }

    private fun showError(message: String?) {
        val text = if (message.isNullOrBlank()) {
            context.getString(R.string.default_exception)
        } else {
            message
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        progressBar?.isVisible = state
        textView?.isVisible = !state
        photo?.isVisible = !state
        refreshButton?.isEnabled = !state
    }

    private fun showContent(model: ScreenState.Model) {
        textView?.text = model.text
        picasso
            .load(model.photoUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .into(photo)
        showLoading(false)
    }

    override fun populate(state: ScreenState) = when(state) {
        is ScreenState.Error -> showError(state.message)
        is ScreenState.Loading -> showLoading(true)
        is ScreenState.Model -> showContent(state)
        is ScreenState.TimeoutException -> showError(context.getString(R.string.timeout_exception))
    }
}

interface ICatsView {
    fun populate(state: ScreenState)
}