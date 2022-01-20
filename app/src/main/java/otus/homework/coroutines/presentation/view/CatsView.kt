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
import otus.homework.coroutines.data.CatModel
import otus.homework.coroutines.presentation.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null
    var onClickAction: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            onClickAction?.invoke()
        }
    }

    override fun populate(cat: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.text
        Picasso.get()
            .load(cat.picture.file)
            .placeholder(R.drawable.ic_baseline_image_search)
            .error(R.drawable.ic_baseline_broken_image)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showToast(msg: String?, msgId: Int?) {
        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else if (msgId != null) {
            Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show()
        }
    }

    override fun <T> process(result: Result<T>) {
        when (result) {
            is Success<*> -> {
                if (result.value is CatModel) {
                    populate(result.value)
                }
            }
            is Error -> {
                showToast(result.msg, result.msgId)
            }
        }
    }
}

interface ICatsView {

    fun populate(cat: CatModel)

    fun showToast(msg: String? = null, msgId: Int? = null)

    fun <T> process(result: Result<T>)
}