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
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    var onClick: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            onClick?.invoke()
        }
        imageView = findViewById(R.id.fact_imageView)
        textView = findViewById(R.id.fact_textView)
    }

    override fun populate(uiModel: CatsUiModel) {
        textView.text = uiModel.fact
        Picasso.get().load(uiModel.image).into(imageView)
    }

    override fun showToast(text: String?) {
        text?.let {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun bindError(message: String?) {
        textView.text = message
        imageView.setImageResource(0)
    }

    fun bindSuccess(uiModel: CatsUiModel) {
        textView.text = uiModel.fact
        Picasso.get().load(uiModel.image).into(imageView)
    }

    fun bindLoading() {
        textView.text = null
        imageView.setImageResource(0)
    }
}

interface ICatsView {

    fun populate(uiModel: CatsUiModel)
    fun showToast(text: String?)
}