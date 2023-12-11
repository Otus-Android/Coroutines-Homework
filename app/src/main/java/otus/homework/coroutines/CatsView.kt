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
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var buttonOnClickDelegate: LoaderDelegate? = null
    private val imageLoader: Picasso by lazy { Picasso.get() }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            buttonOnClickDelegate?.load()
        }
    }

    fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.text
        imageLoader
            .load(cat.imageUrl)
            .into(
                findViewById<ImageView>(R.id.cat_imageView)
            )
    }

    fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun interface LoaderDelegate {
        fun load()
    }
}
