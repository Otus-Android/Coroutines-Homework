package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(item: CatViewItem) {
        findViewById<TextView>(R.id.fact_textView).text = item.fact
        val imageView = findViewById<ImageView>(R.id.picture_imageView)
        Picasso.get().load(item.pictureUrl).into(imageView)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(item: CatViewItem)
    fun showToast(message: String)
}