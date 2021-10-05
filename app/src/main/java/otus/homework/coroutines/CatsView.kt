package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.Cats

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(cats: Cats) {
        findViewById<TextView>(R.id.fact_textView).text = cats.text

        val imageView = findViewById<ImageView>(R.id.image_cat)
        Picasso.get().load(cats.images).into(imageView)
    }

    override fun showToastSomeException(throwable: Throwable) {
        Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_SHORT).show()
    }

    override fun showToastTimeout(throwable: Throwable) {
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cats: Cats)
    fun showToastSomeException(throwable: Throwable)
    fun showToastTimeout(throwable: Throwable)
}