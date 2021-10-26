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
    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            catsViewModel?.onInitComplete()
        }
    }

    override fun populate(data: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact
        Picasso.get().load(data.imgUrl).into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun networkError() {
        showToast(context.resources.getString(R.string.network_error))
    }

    override fun onDataChange(result: Result) {
        when(result) {
            is Success<*> -> if(result.data is CatsData) populate(result.data)
            is Error -> showToast(result.error)
            is NetworkError -> networkError()
        }
    }
}

interface ICatsView {

    fun populate(data: CatsData)
    fun showToast(message: String)
    fun networkError()
    fun onDataChange(result: Result)
}