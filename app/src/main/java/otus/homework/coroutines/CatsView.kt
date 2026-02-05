package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
       imageView = findViewById(R.id.cat_imageView)
    }

    override fun populate(catsInfo: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catsInfo.fact.fact
        Picasso.get()
            .load(catsInfo.img.url)
            .resize(catsInfo.img.width, catsInfo.img.width)
            .into(imageView)
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catsInfo: CatsInfo)
    fun showToast(text: String)
}