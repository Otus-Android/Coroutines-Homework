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
import otus.homework.coroutines.network.dto.CatData

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

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact.text
        val catImagePlaceholder = findViewById<ImageView>(R.id.image_imageView)
        Picasso.get()
            .load(catData.image.imageUrl)
            .into(catImagePlaceholder)
    }

    override fun showErrorToast(@StringRes errorTextRes: Int) {
        val errorText = context.getText(errorTextRes)
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(catData: CatData)
    fun showErrorToast(@StringRes errorTextRes: Int)
}