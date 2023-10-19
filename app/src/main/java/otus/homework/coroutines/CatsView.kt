package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            runBlocking {
                launch {
                    presenter?.onInitComplete()
                }
            }
        }
    }

    override fun populate(catfact: CatFact) {
        findViewById<TextView>(R.id.fact_textView).text = catfact.fact
    }
}

interface ICatsView {

    fun populate(catfact: CatFact)
}