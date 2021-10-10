package otus.homework.coroutines.presentation.mvp

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRandomFact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private lateinit var textview: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()
        textview = findViewById<TextView>(R.id.fact_textView).apply { movementMethod = ScrollingMovementMethod() }
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: CatRandomFact) {
        textview.text = fact.text
        Picasso.get()
            .load(fact.imageUrl)
            .into(findViewById<ImageView>(R.id.cat_image))
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: CatRandomFact)

    fun displayError(errorMessage: String)
}