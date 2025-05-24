package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Deferred

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    private var catImageView: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        catImageView = findViewById(R.id.image)
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            viewModel?.getCat()
        }
    }

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.fact
        Picasso.get().load(cat.image.url).into(findViewById<ImageView>(R.id.image))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun getCatImageView(): ImageView {
        return catImageView ?: throw IllegalStateException("ImageView not initialized")
    }
}

interface ICatsView {

    fun populate(cat: Cat)
    fun showToast(message: String)
    fun getCatImageView(): ImageView
}