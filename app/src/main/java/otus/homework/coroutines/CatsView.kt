package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter: CatsPresenter? = null
    private var refreshButtonListener: () -> Unit = {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
            refreshButtonListener()
        }
    }

    override fun populate(factWithImage: FactWithImage) {
        findViewById<TextView>(R.id.fact_textView).text = factWithImage.fact

        val imageView = findViewById<ImageView>(R.id.catImage_imageView)
        Picasso.get().load(factWithImage.imageUrl).into(imageView)
    }

    override fun showShortToast(@StringRes res: Int) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
    }

    override fun showShortToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun setOnClickRefreshButtonListener(listener: () -> Unit ) {
        refreshButtonListener = listener
    }
}

interface ICatsView {

    fun populate(factWithImage: FactWithImage)

    fun showShortToast(text: String)

    fun showShortToast(@StringRes res: Int)
}