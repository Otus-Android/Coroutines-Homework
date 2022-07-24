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

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(state: CatsViewState) {
        val factTextView = findViewById<TextView>(R.id.fact_textView)
        val photoImageView = findViewById<ImageView>(R.id.photo_imageView)

        factTextView.text = state.fact
        Picasso.get()
            .load(state.fileUrl)
            .into(photoImageView)
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(state: CatsViewState)
    fun showMessage(text: String)
}