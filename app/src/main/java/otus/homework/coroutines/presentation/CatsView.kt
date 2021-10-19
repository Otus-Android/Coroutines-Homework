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
import otus.homework.coroutines.data.CatResult
import otus.homework.coroutines.presentation.model.FactModel

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

    override fun populate(model: CatResult<FactModel>) {
        val textView = findViewById<TextView>(R.id.fact_textView)
        val imageView = findViewById<ImageView>(R.id.image_imageView)

        if (model is CatResult.Success) {
            val data = model.data
            val image = data.image
            val fact = data.fact
            Picasso
                .get()
                .load(image)
                .resize(400, 400)
                .centerCrop()
                .into(imageView)

            textView.text = fact
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(model: CatResult<FactModel>)

    fun showToast(message: String)
}