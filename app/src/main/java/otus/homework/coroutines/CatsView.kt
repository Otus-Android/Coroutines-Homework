package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.Content

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
//    }

    override fun populate(content: Content) {
        findViewById<TextView>(R.id.fact_textView).text = content.fact.text
        Picasso.Builder(context)
            .build()
            .load(content.image.imageString)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(content: Content)

    fun showToast(text: String)
}
