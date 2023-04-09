package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
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

    override fun showToast(message: RealString) {
        Toast.makeText(context, message.toStr(context), Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showToast(message: RealString)
}

sealed interface RealString {
    @JvmInline
    value class Res(@StringRes val resId: Int) : RealString

    @JvmInline
    value class Str(val string: String) : RealString
}

fun RealString.toStr(context: Context) = when (this) {
    is RealString.Res -> context.resources.getString(resId)
    is RealString.Str -> string
}