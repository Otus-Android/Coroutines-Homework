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
            //presenter?.onInitComplete()
            catsViewModel?.fetchCatsInfo()
        }
    }

    override fun populate(info: CatsInfo) {
        findViewById<TextView>(R.id.fact_textView).text = info.fact
        Picasso.get().load(info.imageUrl).into(findViewById<ImageView>(R.id.cat_image_imageView))
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDataChange(result: Result) {
        when(result) {
            is Success<*> -> if(result.data is CatsInfo) populate(result.data)
            is Error -> Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
        }
    }
}

interface ICatsView {

    fun populate(info: CatsInfo)
    fun showError(errMsg: String)
    fun onDataChange(result: Result)
}