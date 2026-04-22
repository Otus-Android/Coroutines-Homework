package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var onButtonClick: (() -> Unit)? = null
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {


    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onButtonClick?.invoke()
        }
    }

    override fun populate(catModels: CatModels) {
        findViewById<TextView>(R.id.fact_textView).text = catModels.fact
        val imageView = findViewById<ImageView>(R.id.cat_image)

        Picasso.get()
            .load(catModels.url)
            .into(imageView)
    }
}

interface ICatsView {
    fun populate(catModels: CatModels)
}