package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.network.facts.base.CatData

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    fun setUpButtonCallback(getFactAndImage: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            getFactAndImage()
        }
    }

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact?.text
        val imageView = findViewById<ImageView>(R.id.fact_image)
        Picasso.get().load(catData.imageUrl?.file).into(imageView);
    }
}

interface ICatsView {
    fun populate(catData: CatData)
}