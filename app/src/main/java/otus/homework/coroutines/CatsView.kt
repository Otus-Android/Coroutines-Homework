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

    private lateinit var factTextView: TextView
    private lateinit var randomImageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        factTextView = findViewById(R.id.fact_textView)
        randomImageView = findViewById(R.id.random_imageView)
    }

    override fun populate(catsModel: CatsModel) {
        Picasso.get().load(catsModel.randomImageUrl).into(randomImageView)
        factTextView.text = catsModel.fact
    }

    override fun showError(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    override fun showNetworkError() =
        showError(context.getString(R.string.network_connection_error))
}

interface ICatsView {

    fun populate(catsModel: CatsModel)

    fun showError(msg: String)

    fun showNetworkError()
}