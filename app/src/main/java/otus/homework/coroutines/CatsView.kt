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

    override fun populate(cat: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.text
        Picasso.get()
            .load(cat.picture.file)
            .placeholder(R.drawable.ic_baseline_image_search)
            .error(R.drawable.ic_baseline_broken_image)
            .into(findViewById<ImageView>(R.id.cat_imageView))
    }

    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(msgId: Int) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(cat: CatModel)

    fun showToast(msg: String)

    fun showToast(msgId: Int)
}