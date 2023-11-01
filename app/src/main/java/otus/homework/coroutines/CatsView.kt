package otus.homework.coroutines

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

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

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.text
        findViewById<ImageView>(R.id.cat_imageView).setImageBitmap(cat.image)
    }

    override fun showErrorToast(message: String?) {
        val actualMessage = message ?: "Не удалось получить ответ от сервера"
        Toast.makeText(context, actualMessage, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: Cat)

    fun showErrorToast(message: String? = null)
}