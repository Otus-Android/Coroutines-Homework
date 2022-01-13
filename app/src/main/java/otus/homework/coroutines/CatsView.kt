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

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(uiData: CatsViewUiData) {
        findViewById<ImageView>(R.id.picture_imageView).let { imageView ->
            Picasso.get()
                .load(uiData.picture.url)
                .into(imageView)
        }

        findViewById<TextView>(R.id.fact_textView).text = uiData.fact.text
    }

    override fun makeToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    override fun setOnFactRequestListener(listener: () -> Unit) {
        findViewById<Button>(R.id.button).setOnClickListener {
            listener.invoke()
        }
    }
}

data class CatsViewUiData(val fact: Fact, val picture: Picture)

interface ICatsView {
    fun populate(uiData: CatsViewUiData)
    fun makeToast(string: String)
    fun setOnFactRequestListener(listener : () -> Unit)
}
