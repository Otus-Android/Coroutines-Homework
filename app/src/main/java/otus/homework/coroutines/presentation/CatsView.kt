package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onClick()
        }
    }

    override fun populate(model: CatInfoModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.text
        Picasso.get()
            .load(model.iconUrl)
            .resize(model.width, model.height)
            .centerCrop()
            .into(findViewById<ImageView>(R.id.image_imageView))
    }

    override fun showToast(text: Text) {
        Toast.makeText(context, text.getString(context), Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(model: CatInfoModel)
    fun showToast(text: Text)
}