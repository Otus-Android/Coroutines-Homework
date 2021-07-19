package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.entity.Animal

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(animal: Animal) {
        findViewById<TextView>(R.id.fact_textView).text = animal.text

        val imageView = findViewById<ImageView>(R.id.cat_image)
        Picasso.get()
            .load(animal.images)
            .placeholder(R.drawable.ic_error)
            .error(R.drawable.ic_error)
            .into(imageView)
    }
}

interface ICatsView {
    fun populate(animal: Animal)
}