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

    override fun populate(data: PopulateData) {
        findViewById<TextView>(R.id.fact_textView).text = data.factText
        Picasso.get()
            .load("https://cataas.com"+data.imageCat)
            .into( findViewById<ImageView>(R.id.pic_imageView))
    }

    override fun error(errorText: String) {
       Toast.makeText(context,errorText,Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(data: PopulateData)

    fun error(errorText : String)
}