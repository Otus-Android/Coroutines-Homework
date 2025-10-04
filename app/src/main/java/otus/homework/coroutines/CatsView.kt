package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Image
import otus.homework.coroutines.model.Model

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
            if (presenter != null) {
                presenter?.onInitComplete()
            } else {
                viewModel?.onInitComplete()
            }
        }
    }

    override fun populate(model: Model) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.fact
        val Imageview = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(model.imageUrl).into(Imageview)
    }
}

interface ICatsView {

    fun populate(model: Model)
}