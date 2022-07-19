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

    override fun onFinishInflate() {
        super.onFinishInflate()

        //uncomment to use CatsPresenter
//        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
//        }
    }

    override fun populate(content: CatsContent) {
        content.fact?.let { findViewById<TextView>(R.id.fact_textView).text = it.text }
        val imageView = findViewById<ImageView>(R.id.image)
        content.imageUrl?.let {
            Picasso.get().load(it.fileUrl).into(imageView)
        }
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {

    fun populate(content: CatsContent)

    fun showToast(text: String)
}