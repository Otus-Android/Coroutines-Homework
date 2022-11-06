package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.CatDataModel
import otus.homework.coroutines.mvp.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catData: CatDataModel) {
        catData.fact?.let { findViewById<TextView>(R.id.fact_textView).text = catData.fact.text }
        catData.picUrl?.let{
            Picasso.get()
                .load(catData.picUrl.file)
                .into(findViewById<ImageView>(R.id.pic_imageView));
        }
    }

    override fun showSocketTimeoutException() {
        showToast(context.getString(R.string.socket_imeout_exeption_text))
    }

    override fun showCancellationException() {
        showToast(context.getString(R.string.cancellation_exeption_text))
    }

    override fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catData: CatDataModel)
    fun showSocketTimeoutException()
    fun showCancellationException()
    fun showToast(message: String)

}