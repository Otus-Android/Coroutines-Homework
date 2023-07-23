package otus.homework.coroutines.presentation.mvp

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatModel

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

    override fun populate(cat: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        Picasso.get().load(cat.imageUrl).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showError() {
        Toast.makeText(this.context, R.string.error, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: CatModel)

    fun showError()
}
