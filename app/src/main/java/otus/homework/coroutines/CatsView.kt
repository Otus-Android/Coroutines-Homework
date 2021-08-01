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
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override suspend fun populate(catData: CatData) {
        Picasso.get().load(catData.image.await().fileUrl).into(findViewById<ImageView>(R.id.image_imageView))
        findViewById<TextView>(R.id.fact_textView).text = catData.fact.await().firstOrNull()?.fact
    }

    override fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    suspend fun populate(catData: CatData)
    fun showToast(s: String)
}