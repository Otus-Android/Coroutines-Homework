package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
    }

    override fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(strRes: Int) {
        Toast.makeText(context, strRes, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)

    fun showMessage(text: String)

    fun showMessage(strRes: Int)
}