package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.Result

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    //var presenter: CatsPresenter? = null
    private var onRefresh: (() -> Unit)? = null

    fun setRefreshListener(l: () -> Unit) {
        onRefresh = l
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onRefresh?.invoke()
        }
    }

    override fun render(state: Result) {
        when (state) {
            is Result.Loading -> {
                findViewById<Button>(R.id.button).isEnabled = false
            }

            is Result.Error -> {
                val errorMessage = findViewById<TextView>(R.id.error_textView)
                errorMessage.visibility = VISIBLE
                errorMessage.text = state.message
                findViewById<Button>(R.id.button).isEnabled = true
            }

            is Result.Success<*> -> {
                val data = (state.data as? CatsFactState)
                findViewById<TextView>(R.id.fact_textView).text = data?.fact
                if (data?.imageUrl?.isNotEmpty() == true) {
                    Picasso.get()
                        .load(data.imageUrl)
                        .into(findViewById<ImageView>(R.id.cat_imageView))
                }
                findViewById<Button>(R.id.button).isEnabled = true
                findViewById<TextView>(R.id.error_textView).visibility = GONE
            }
        }
    }
}

interface ICatsView {

    fun render(state: Result)
}