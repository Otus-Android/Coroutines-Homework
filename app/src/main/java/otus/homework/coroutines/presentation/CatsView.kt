package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.catsJobCancel()
            presenter?.onInitComplete()
        }
    }

    override fun populate(model: CatsInfo) {
        val textView = findViewById<TextView>(R.id.fact_textView)
        textView.text = model.fact
        val imageView = findViewById<ImageView>(R.id.cat_image)
        Picasso.get().load(model.imageUrl).into(imageView)
    }

    override fun onButtonClick(onClick: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener { onClick() }
    }
}

interface ICatsView {

    fun populate(model: CatsInfo)

    fun onButtonClick(onClick: () -> Unit)
}
