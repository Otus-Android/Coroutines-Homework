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
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
            //presenter?.onInitComplete()
        }
    }

    override fun populate(pair: Pair<Fact, Picture>) {
            findViewById<TextView>(R.id.fact_textView).text = pair.first.text
            Picasso.get().load(pair.second.picUrl)
                .resize(500, 500)
                .centerCrop()
                .into(findViewById<ImageView>(R.id.pic_imageView))
    }

    override fun showToastFromRes(res: Int) {
        Toast.makeText(context, context.getString(res), Toast.LENGTH_SHORT).show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(pair: Pair<Fact, Picture>)
    fun showToastFromRes(res: Int)
    fun showToast(message: String?)
}