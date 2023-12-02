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

    var viewModel: MainViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(result: Result?) {//fact: Fact, link: String) {
        when (result) {
            is Result.Success -> {
                findViewById<TextView>(R.id.fact_textView).text = result.fact.text
                Picasso.get().load(result.link).into(findViewById<ImageView>(R.id.fact_ImageView))
            }

            is Result.Error -> {
                findViewById<TextView>(R.id.fact_textView).text = result.error.message
            }

            else -> {}
        }
    }
}

interface ICatsView {

    fun populate(result: Result?)//fact: Fact, link: String)
}
