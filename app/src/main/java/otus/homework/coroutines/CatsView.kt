package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    //var presenter: CatsPresenter? = null

    var viewModel : CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(state: Result) {
        when (state) {
            is Result.Success -> showContent(state.uiState)
            is Result.Error -> showError(state.message)
        }
    }

    private fun showContent(state: UiState) {
        findViewById<TextView>(R.id.fact_textView).text = state.fact
        Picasso.get().load(state.imageUrl).into(findViewById<ImageView>(R.id.meow_image))
    }

    private fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(state: Result)
}