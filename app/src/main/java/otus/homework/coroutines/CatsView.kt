package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.CatData
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Image

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    var imageView: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        imageView = findViewById(R.id.cat_image_view)
    }

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact.text
        Picasso.get().load(catData.imagePath).into(imageView!!)
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(strRes: Int) {
        Toast.makeText(context, strRes, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(catData: CatData)

    fun showMessage(text: String)

    fun showMessage(strRes: Int)
}