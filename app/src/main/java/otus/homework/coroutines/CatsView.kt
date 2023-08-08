package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(result: Result) {

        val txt = findViewById<TextView>(R.id.fact_textView)
        val img = findViewById<ImageView>(R.id.imageView)

        when (result) {
            is Loading -> {
                txt.text = "Loading..."
                img.isVisible = false
            }
            is Success -> {
                txt.text = result.fact.fact
                Picasso.get().load(result.fact.image).into(img)
                img.isVisible = true
            }
            is Error -> {
                txt.text = result.message
                img.isVisible = false
            }
        }
    }
}

interface ICatsView {

    fun populate(result: Result)
}
