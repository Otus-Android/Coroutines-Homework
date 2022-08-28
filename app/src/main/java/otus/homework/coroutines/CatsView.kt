package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Picasso
import otus.homework.coroutines.network.Fact
import otus.homework.coroutines.presentation.CatModel
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.CatsViewModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact?.text.orEmpty()

        val catImageView = findViewById<ImageView>(R.id.catImage)
        Picasso.get()
            .load(catModel.image?.catImage)
            .into(catImageView)
    }

    override fun showUILoading(isLoading: Boolean) {
        if (isLoading) {
            findViewById<TextView>(R.id.fact_textView).text = "Идет загрузка данных..."
        }

        findViewById<ImageView>(R.id.catImage).isVisible = !isLoading
        findViewById<Button>(R.id.button).isVisible = !isLoading

        findViewById<Button>(R.id.loadingProgress).isVisible = isLoading
    }

    override fun showError(errorMessage: String?) {
        findViewById<TextView>(R.id.fact_textView).text = ""
        Toast.makeText(context, errorMessage ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)

    fun showUILoading(isLoading: Boolean)

    fun showError(errorMessage: String?)
}