package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    //    var presenter: CatsPresenter? = null
    var viewModel: CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(fact: FactAndImageModel) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        Picasso.get().load(fact.image).into(findViewById<ImageView>(R.id.image))
    }
}

interface ICatsView {

    fun populate(fact: FactAndImageModel)
}