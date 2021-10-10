package otus.homework.coroutines.presentation.mvp

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRandomFact
import otus.homework.coroutines.domain.Result

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private lateinit var textview: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var button: Button

    override fun onFinishInflate() {
        super.onFinishInflate()
        progressBar = findViewById(R.id.progress)
        textview = findViewById<TextView>(R.id.fact_textView).apply { movementMethod = ScrollingMovementMethod() }
        button = findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                presenter?.onInitComplete()
            }
        }
    }

    override fun populate(result: Result<CatRandomFact>) {
        when (result) {
            is Result.Success -> displayData(result.value)
            is Result.Error -> displayError(result.errorMessage.orEmpty())
        }
    }

    private fun displayData(fact: CatRandomFact) {
        textview.text = fact.text
        Picasso.get()
            .load(fact.imageUrl)
            .into(findViewById<ImageView>(R.id.cat_image))
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        button.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        button.isEnabled = true
        progressBar.visibility = View.GONE
    }
}

interface ICatsView {

    fun populate(result: Result<CatRandomFact>)

    fun displayError(errorMessage: String)

    fun showProgress()

    fun hideProgress()
}