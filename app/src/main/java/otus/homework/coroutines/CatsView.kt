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

    override fun populate(fact: Fact,img:Img) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text

        val imgV = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(img.img).into(imgV)

    }
    override fun message(txt: String) {
        Toast.makeText(this.context, txt, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact,img:Img)
    fun message(txt:String)
}