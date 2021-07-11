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
    var viewModel: CatsViewModel? = null
    private lateinit var factTextView: TextView
    private lateinit var factImageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
        factTextView = findViewById(R.id.fact_textView)
        factImageView = findViewById(R.id.fact_imageView)
    }

    override fun populate(factResult: Result.Success<PresentationFact>) {
        factTextView.text = factResult.value.fact
        Picasso.get()
            .load(factResult.value.imageUrl)
            .into(factImageView)
    }

    override fun showError(errorResult: Result.Error) {
        if (errorResult.message == null) {
            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, errorResult.message, Toast.LENGTH_SHORT).show()
        }
    }
}

interface ICatsView {

    fun populate(factResult: Result.Success<PresentationFact>)

    fun showError(errorResult: Result.Error)
}