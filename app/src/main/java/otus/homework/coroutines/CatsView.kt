package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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

    override fun populate(catResult: CatResult) {
        findViewById<TextView>(R.id.fact_textView).text = catResult.fact.text
        loadImage(catResult.imageUrl.url, findViewById(R.id.cat_imageView))
    }

    private fun loadImage(url: String, view: ImageView) {
        Picasso
            .get()
            .load(url)
            .into(view)
    }
}

interface ICatsView {

    fun populate(catResult: CatResult)
}
