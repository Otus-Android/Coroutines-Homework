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

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun toast(exception: String) {
        Toast.makeText(context, exception, Toast.LENGTH_LONG ).show()
    }

    override fun populate(info: CatResponse) {
        findViewById<TextView>(R.id.fact_textView).text = info.catFact.fact

    }
}

interface ICatsView {

    fun populate(info: CatResponse)
    fun toast(exception : String)
}