package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.domain.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {


    var callback:(()->Unit)? = null
    lateinit var button: Button

    override fun onFinishInflate() {
        super.onFinishInflate()
        button = findViewById(R.id.button)
        button.setOnClickListener{callback?.invoke()}
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
    }
}

interface ICatsView {

    fun populate(fact: Fact)
}