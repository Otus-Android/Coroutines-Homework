package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

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
            //presenter?.onInitComplete()
            viewModel?.getCatFactsAndImage()
        }
    }

    override fun populate(fact: Fact) {

        findViewById<TextView>(R.id.fact_textView).text = fact.text

        if (fact.file.isNotBlank() && fact.file.isNotEmpty()) {
            Picasso
                .get()
                .load(fact.file)
                //.placeholder(R.drawable.ic_baseline_check_box_outline_blank_24)
                .error(R.drawable.ic_baseline_clear_24)
                //.resize(600, 600)
                //.fit()
                .into(findViewById<ImageView>(R.id.fact_imageView) as ImageView)
        }
    }
}

interface ICatsView {
    fun populate(fact: Fact)
}