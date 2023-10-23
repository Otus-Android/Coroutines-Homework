package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatResponse

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun toast(exception: String) {
        Toast.makeText(context, exception, Toast.LENGTH_LONG ).show()
    }

    override fun populate(response: CatResponse) {
        findViewById<TextView>(R.id.fact_textView).text = response.catFact.fact
        val imagePoster: ImageView = findViewById<ImageView>(R.id.cat_image)
        Picasso.get()
            .load(response.catImage)
            .into(imagePoster)

    }
}

interface ICatsView {

    fun populate(response: CatResponse)
    fun toast(exception : String)
}