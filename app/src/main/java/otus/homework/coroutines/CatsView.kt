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


    override fun onFinishInflate() {
        super.onFinishInflate()
    }
    fun loadImageFromUrl(url: String, imageView: ImageView) {
        Picasso.get()
            .load(url)
            .into(imageView)
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        loadImageFromUrl(fact.url, findViewById<ImageView>(R.id.cat_imgView))
    }

    override fun showServerErrorToast() {
        Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showServerErrorToast()
    fun showToast(message: String)
}