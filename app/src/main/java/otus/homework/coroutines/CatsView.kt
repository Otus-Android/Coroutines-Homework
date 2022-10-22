package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel :CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete(context)
        }
    }

    override fun populate(catsData: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsData.text
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(catsData.image).into(imageView)
    }
}

interface ICatsView {

    fun populate(catsData: CatsData)
}