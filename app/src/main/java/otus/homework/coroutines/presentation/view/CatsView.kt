package otus.homework.coroutines.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.data.Cat
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.presentation.presentor.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null
    var onClick: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            onClick?.invoke()
        }
    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.text
        Picasso.get()
            .load(cat.picture)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun render(result: Result<Cat>) {
        when (result) {
            is Result.Success<Cat> -> {
                populate(result.value)
            }
            is Result.Error -> {
                showToast(result.message)
            }
        }
    }
}

interface ICatsView {
    fun populate(cat: Cat)
    fun showToast(message: String?)
    fun render(result: Result<Cat>)
}