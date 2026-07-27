package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.FullCatFact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var onRefresh: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onRefresh?.invoke()
        }
    }

    override fun populate(fullFact: FullCatFact) {
        findViewById<TextView>(R.id.fact_textView).text = fullFact.fact.fact
        val view = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get().load(fullFact.imageUrl).into(view)
    }

    override fun showErrorToast(errorText: String) {
        Toast(context)
            .apply {
                setText(errorText)
                duration = Toast.LENGTH_LONG
            }.show()
    }
}

interface ICatsView {

    fun populate(fullFact: FullCatFact)

    fun showErrorToast(errorText: String)
}
