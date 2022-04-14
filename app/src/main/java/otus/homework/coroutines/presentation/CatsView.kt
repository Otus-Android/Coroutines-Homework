package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.CatDto
import otus.homework.coroutines.presentation.CatsError
import otus.homework.coroutines.presentation.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun displayError(error: CatsError) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun populate(catDto: CatDto) {
        findViewById<TextView>(R.id.fact_textView).text = catDto.fact.text
        Picasso.get()
            .load(catDto.photoUrl)
            .into(findViewById<ImageView>(R.id.catPhoto))
    }
}

interface ICatsView {
    fun displayError(error: CatsError)
    fun populate(catDto: CatDto)
}