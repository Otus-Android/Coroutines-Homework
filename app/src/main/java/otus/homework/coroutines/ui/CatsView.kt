package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.R
import otus.homework.coroutines.dtos.Fact
import otus.homework.coroutines.presentation.CatsPresenter

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
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun postWarning(messageProvider: Context.() -> String) {
        post {
            Toast
                .makeText(context, messageProvider.invoke(context), Toast.LENGTH_SHORT)
                .show()
        }
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun postWarning(messageProvider: Context.() -> String)
}