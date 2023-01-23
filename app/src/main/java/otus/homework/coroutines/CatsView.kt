package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.Cat
import otus.homework.coroutines.data.Fact

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

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        val image = findViewById<ImageView>(R.id.imageView)
        Picasso.with(context)
            .load(cat.file)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_error)
            .into(image)
    }
}

interface ICatsView {

    fun populate(cat: Cat)
}