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

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun showResult(result: Result) {
        when (result) {
            is Success -> populate(result.value)
            is Error -> showError(result.message)
        }
    }

    override fun populate(info: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = info.fact
        loadImage(info.image)
    }

    private fun loadImage(url: String) {
        val imageView = findViewById<ImageView>(R.id.cats_imageView)
        Picasso.get()
            .load(url)
            .into(imageView)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {
    fun populate(info: CatsInfo)
    fun showError(message: String)
    fun showResult(result: Result)
}