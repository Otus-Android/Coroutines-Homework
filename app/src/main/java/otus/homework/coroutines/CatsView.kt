package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

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

    override fun populate(catPresentationModel: CatPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text =
            catPresentationModel.catFactMessage
        val imageView = findViewById<ImageView>(R.id.cat_iv)
        Picasso.get()
            .load(catPresentationModel.catImageUrl)
            .into(imageView)
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(catPresentationModel: CatPresentationModel)
    fun showMessage(msg: String)
}