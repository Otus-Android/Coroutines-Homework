package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact
        Picasso.get().load(catData.image).into(
            findViewById<ImageView>(R.id.image)
        )
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showToast(stringRes: Int) {
        Toast.makeText(
            context,
            context.getString(stringRes),
            Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {

    fun populate(catData: CatData)

    fun showToast(message: String?)

    fun showToast(@StringRes stringRes: Int)
}