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

    //var presenter: CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onViewInitializationComplete()
        }
    }

    override fun populate(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact.fact
        Picasso.get().load(catModel.file.file).into(findViewById<ImageView>(R.id.fact_imageView))
    }

    override fun showSocketExceptionMessage() {
        Toast.makeText(
            context,
            context.getString(R.string.socketTimeoutExceptionText),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showExceptionMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catModel: CatModel)
    fun showSocketExceptionMessage()
    fun showExceptionMessage(message: String)
}