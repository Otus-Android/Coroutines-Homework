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

    override fun populate(imagedFact: ImagedFact) {
        findViewById<TextView>(R.id.fact_textView).text = imagedFact.factString
        Picasso.get().load(imagedFact.imageUrl).into(findViewById<ImageView>(R.id.image))
    }

    override fun showAlert(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showAlert(id: Int) {
        val message = context.getString(id)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(imagedFact: ImagedFact)
    fun showAlert(message: String)
    fun showAlert(id: Int)
}
