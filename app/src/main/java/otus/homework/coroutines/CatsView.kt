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

    private var button: Button? = null
    private var tvFact: TextView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvFact = findViewById<TextView>(R.id.fact_textView)

        button = findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                presenter?.onInitComplete()
            }
        }
    }

    override fun populate(fact: Fact) {
        tvFact?.text = fact.text
    }

    override fun showServerError() {
        val serverErrorMessage = context.getString(R.string.server_error_message)
        val toast = Toast.makeText(context, serverErrorMessage, Toast.LENGTH_LONG)

        toast.show()
    }

    override fun showDefaultError(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)

        toast.show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)

    fun showServerError()

    fun showDefaultError(message: String)
}