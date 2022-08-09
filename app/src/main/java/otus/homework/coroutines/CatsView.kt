package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatsInfo

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel?.onInitComplete()
        }
    }

    override fun populate(model: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        findViewById<ImageView>(R.id.cat_imageView).also {
            Picasso.get().load(model.image).into(it)
        }
    }

    override fun displayErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(model: CatsInfo)

    fun displayErrorMessage(message: String)
}