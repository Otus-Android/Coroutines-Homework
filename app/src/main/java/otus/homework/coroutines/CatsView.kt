package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.model.CatModel

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter :CatsPresenter? = null
    var viewModel :CatViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact
        val imageView = findViewById<ImageView>(R.id.random_cat)
        Picasso.get().load(catModel.imageUrl).into(imageView)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)
    fun showToast(message: String)
}
