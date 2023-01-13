package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatsUiState

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    val button by lazy { findViewById<Button>(R.id.button) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        button.setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(state: CatsUiState) {
        findViewById<TextView>(R.id.fact_textView).text = state.fact
        val imageView = findViewById<ImageView>(R.id.cat_image_view)
        Picasso.get()
            .load(state.imageUrl)
            .into(imageView)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun loadingData(isLoading: Boolean) {
        findViewById<TextView>(R.id.progress_bar).isVisible = isLoading
        button.isEnabled = !isLoading
    }
}

interface ICatsView {
    fun populate(state: CatsUiState)
    fun showToast(message: String)
    fun loadingData(isLoading: Boolean)
}