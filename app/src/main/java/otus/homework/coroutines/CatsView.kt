package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            if (presenter != null) {
                presenter?.onInitComplete()
            } else {
                viewModel?.onInitComplete()
            }
        }
    }

    override fun populate(data: CatDataModel) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact.fact
        val imageView = findViewById<ImageView>(R.id.imageContainer)
        Picasso.get().load(data.imageUrl).into(imageView)
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message ?: "Неизвестная ошибка", Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(data: CatDataModel)
    fun showToast(message: String?)
}
