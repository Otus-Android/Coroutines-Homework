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

//    var presenter: CatsPresenter? = null
    var action: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
            action?.invoke()
        }
    }

    override fun populate(fact: Fact, image: Image) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        val pic = findViewById<ImageView>(R.id.pic_imageView)
        Picasso.get()
            .load(image.src)
            .into(pic)
    }

    override fun populate(info: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = info.fact
        val pic = findViewById<ImageView>(R.id.pic_imageView)
        Picasso.get()
            .load(info.image)
            .into(pic)
    }

    override fun show(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(fact: Fact, image: Image)
    fun populate(info: Cat)
    fun show(message: String)
}