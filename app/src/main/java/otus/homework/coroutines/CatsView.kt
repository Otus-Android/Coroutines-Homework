package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.network.Fact
import otus.homework.coroutines.presentation.CatModel
import otus.homework.coroutines.presentation.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact?.text

        val catImageView = findViewById<ImageView>(R.id.catImage)
        Picasso.get()
            .load(catModel.image?.catImage)
            .into(catImageView)
    }

    override fun showError(errorMessage: String?) {
        Toast.makeText(context, errorMessage ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)

    fun showError(errorMessage: String?)
}