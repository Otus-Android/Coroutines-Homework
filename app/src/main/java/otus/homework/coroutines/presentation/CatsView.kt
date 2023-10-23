package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.presentation.CatViewModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var catViewModel: CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            catViewModel?.onInitComplete()
        }
    }

    override fun populate(model: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.fact

        Picasso.get()
            .load(model.image?.imageUrl)
            .into(findViewById<ImageView>(R.id.iv_cat))
    }
}

interface ICatsView {
    fun populate(model: CatModel)
}