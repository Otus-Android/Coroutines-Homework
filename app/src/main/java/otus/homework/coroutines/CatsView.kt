package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(factWithPhoto: FactWithPhoto) {
        findViewById<TextView>(R.id.fact_textView).text = factWithPhoto.fact.fact
        Picasso.get()
            .load(factWithPhoto.photo)
            .into(findViewById<ImageView>(R.id.image))
    }

}

interface ICatsView {
    fun populate(factWithPhoto: FactWithPhoto)

}