package otus.homework.coroutines.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CatsViewModel
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(cat: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = cat.title
        findViewById<ImageView>(R.id.image).apply {
            Picasso.get().load(cat.imgUrl).into(this)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(stringResId: Int) {
        Toast.makeText(context, context.getString(stringResId), Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(cat: CatModel)
    fun showToast(message: String)
    fun showToast(@StringRes stringResId: Int)
}