package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.presenter.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private var toast: Toast? = null

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact.text
        val catImageView = findViewById<ImageView>(R.id.cat_picture)
        Picasso.get()
            .load(catModel.catPic.url)
            .into(catImageView)
    }

    override fun showToast(message: String) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)

    fun showToast(message: String)
}