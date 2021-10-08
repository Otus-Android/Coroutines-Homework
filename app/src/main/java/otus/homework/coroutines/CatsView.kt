package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
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

    override fun populate(catUiModel: CatUiModel) {
        findViewById<TextView>(R.id.fact_textView).text = catUiModel.fact
        Picasso
            .get()
            .load(catUiModel.pictureUrl)
            .into(findViewById<ImageView>(R.id.ivCatFact))
    }

    override fun showToast(message: Int) {
        showToast(resources.getString(message))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {

    fun populate(catUiModel: CatUiModel)

    fun showToast(@StringRes message: Int)

    fun showToast(message: String)

}