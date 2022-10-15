package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.network.facts.abs.AbsCatFact
import otus.homework.coroutines.network.facts.old.Fact

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

    override fun populate(fact: AbsCatFact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
    }

    override fun showError(id: Int?) {
        val errorText = context.getString(id ?: R.string.unknown_error_text)
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    override fun showError(msg: String?) {
        Toast.makeText(
            context,
            msg ?: context.getString(R.string.unknown_error_text),
            Toast.LENGTH_SHORT
        ).show()
    }

}

interface ICatsView {

    fun populate(fact: AbsCatFact)

    fun showError(id: Int? = null)
    fun showError(msg: String? = null)
}