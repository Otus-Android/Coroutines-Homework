package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.Result.Error
import otus.homework.coroutines.Result.Success

interface ICatsView {
    fun populate(result: Result)
    fun showLoadError(message: String)
}

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    interface Callback {
        fun onMoreFacts()
    }

    var callback: Callback? = null

    private val moreFactsBtn by lazy { findViewById<Button>(R.id.catMoreFactsBtn) }
    private val catPhoto by lazy { findViewById<ImageView>(R.id.catPhoto) }
    private val factText by lazy { findViewById<TextView>(R.id.catFactText) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        moreFactsBtn.setOnClickListener { callback?.onMoreFacts() }
    }

    override fun populate(result: Result) = when (result) {
        is Success -> {
            Picasso.get().load(result.data.photoUrl).into(catPhoto)
            factText.text = result.data.text
        }
        is Error -> showError(result.message)
    }

    override fun showLoadError(message: String) = showError(message)

    private fun showError(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
