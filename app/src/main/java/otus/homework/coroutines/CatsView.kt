package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

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

    override fun showError(error: ICatsView.Error) {
        val message = when (error) {
            ICatsView.Error.ServerConnectionError -> context.getString(R.string.connection_error)
            is ICatsView.Error.UnknownError -> error.message
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {
    fun populate(fact: Fact)
    fun showError(error: Error)

    sealed interface Error {
        object ServerConnectionError : Error
        class UnknownError(val message: String) : Error
    }
}