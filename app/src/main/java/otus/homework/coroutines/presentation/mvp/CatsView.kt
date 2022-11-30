package otus.homework.coroutines.presentation.mvp

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.presentation.CatsDataUI
import otus.homework.coroutines.R

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catsDataUI: CatsDataUI) {
        val fact_img = findViewById<ImageView>(R.id.fact_img)
        findViewById<TextView>(R.id.fact_textView).text = catsDataUI.fact
        Picasso.get()
            .load(catsDataUI.url)
            .into(fact_img)

    }
}

interface ICatsView {

    fun populate(catsDataUI: CatsDataUI)
}