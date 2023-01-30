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

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }

    override fun showException(e: Exception) {
       if(e is java.net.SocketTimeoutException) {
           val text = "Не удалось получить ответ от сервера"
           Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }else{
           CrashMonitor.trackWarning(e)
           Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
       }

    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showException(e:Exception)
}