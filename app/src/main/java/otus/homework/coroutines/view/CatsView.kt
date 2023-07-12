package otus.homework.coroutines.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.entity.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    fun onMoreButtonCallback(callback: () -> Unit) {
        findViewById<Button>(R.id.button)
            .setOnClickListener { callback.invoke() }
    }

    fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get()
            .load(fact.imageUrl)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }
}
