package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
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

    override fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.catFactText
        val imageView = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get()
            .load(catInfo.catImageUrl)
            .into(imageView)
    }

    override fun showToast(message: String?) {
        if (message?.isBlank() == true) return
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(catInfo: CatInfo)
    fun showToast(message: String?)
}