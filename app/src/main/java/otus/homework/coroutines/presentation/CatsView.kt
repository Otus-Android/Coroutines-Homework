package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.presentation.CatViewModel
import otus.homework.coroutines.presentation.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    var catViewModel: CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //catViewModel?.onInitComplete()
            presenter?.onInitComplete()
        }
    }

    override fun populate(model: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.fact

        Picasso.get()
            .load(model.image?.imageUrl)
            .into(findViewById<ImageView>(R.id.iv_cat))
    }

    override fun showExceptionMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(model: CatModel)

    fun showExceptionMessage(text: String)
}