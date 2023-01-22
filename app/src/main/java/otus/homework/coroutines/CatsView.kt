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

    private var presenter: CatsPresenter? = null
    private var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            viewModel?.onInitComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact
        Picasso.get().load(catModel.imageUrl).into(findViewById<ImageView>(R.id.cat_image))
    }

    override fun showToast(text: String) {
        val displayedText = text.ifEmpty { context.resources.getString(R.string.exception_message) }
        Toast.makeText(context, displayedText, Toast.LENGTH_LONG).show()
    }

    fun setViewContent(catModel: CatModel) {
        populate(catModel)
    }

    fun setToast(text: String) {
        showToast(text)
    }

    fun initPresenter(presenter: CatsPresenter) {
        this.presenter = presenter
    }

    fun initViewModel(viewModel: CatsViewModel) {
        this.viewModel = viewModel
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)
    fun showToast(text: String = "")
}