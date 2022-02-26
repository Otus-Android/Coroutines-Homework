package otus.homework.coroutines.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatImg
import otus.homework.coroutines.model.CatItem

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    fun buttonClick(callback: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            callback.invoke()
        }
    }

    override fun populate(item: CatItem) {
        findViewById<TextView>(R.id.fact_textView).text = item.fact.text
        Picasso
            .get()
            .load(item.catImg.image)
            .into(findViewById<ImageView>(R.id.cat_img))
    }

    override fun populate(fact: Fact, catImg: CatImg) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso
            .get()
            .load(catImg.image)
            .into(findViewById<ImageView>(R.id.cat_img))
    }

    override fun showError(message: String?) {
        Toast.makeText(
            context,
            message ?: "",
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {
    fun populate(item: CatItem)
    fun populate(fact: Fact, catImg: CatImg)
    fun showError(message: String?)
}