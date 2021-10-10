package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CatsViewModel.Result
import otus.homework.coroutines.CatsViewModel.Result.Error
import otus.homework.coroutines.CatsViewModel.Result.Success

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    interface Callback {
        fun onMoreFacts()
    }

    var callback: Callback? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener { callback?.onMoreFacts() }
    }

    override fun populate(result: Result) = when (result) {
        is Success -> {
            Picasso.get()
                .load(result.data.photoUrl)
                .into(findViewById<ImageView>(R.id.catPhoto))
            findViewById<TextView>(R.id.fact_textView).text = result.data.text
        }
        is Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(result: Result)
}
