package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewModel: CatsViewModel

    private lateinit var factTextView: TextView
    private lateinit var randomImageView: ImageView
    private lateinit var moreFactButton: Button

    override fun onFinishInflate() {
        super.onFinishInflate()
        factTextView = findViewById(R.id.fact_textView)
        randomImageView = findViewById(R.id.random_imageView)
        moreFactButton = findViewById(R.id.button)
        moreFactButton.setOnClickListener {
            viewModel.fetchCatsModel()
        }
    }

    fun setViewModel(viewModel: CatsViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        this.viewModel.state.observe(lifecycleOwner) { result ->
            when(result) {
                is Result.Success -> populate(result.data)
                is Result.Error -> handleError(result.exception)
            }
        }
    }

    private fun handleError(exception: Throwable) {
        when (exception) {
            is SocketTimeoutException -> showNetworkError()
            else -> showError(exception.message.orEmpty())
        }
    }

    private fun populate(catsModel: CatsModel) {
        Picasso.get().load(catsModel.randomImageUrl).into(randomImageView)
        factTextView.text = catsModel.fact
    }

    private fun showError(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    private fun showNetworkError() =
        showError(context.getString(R.string.network_connection_error))
}