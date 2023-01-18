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

    override fun catsMessage(message: String?) {
        message?.let{Toast.makeText(context,message, Toast.LENGTH_SHORT).show()}
            ?: Toast.makeText(context,"Что-то пошло не так ...", Toast.LENGTH_SHORT).show()
    }

//    override fun populate(fact: Fact) {
//        findViewById<TextView>(R.id.fact_textView).text =  fact.fact
//    }
//
//    override fun catsImage(image: CatsImage) {
//        Picasso.get()
//            .load(image.url)
//            .resize(150, 150)
//            .into(findViewById<ImageView>(R.id.imageView)
//                )
//    }

    override fun <T>populate(param: T) {
        when(param) {
            is Fact ->  findViewById<TextView>(R.id.fact_textView).text =  param.fact
            is CatsImage -> {
                        Picasso.get()
                            .load(param.url)
                            .resize(150, 150)
                            .into(findViewById<ImageView>(R.id.imageView)
                                )
            }
            else -> {
                throw IllegalArgumentException("Unknown class to populate")
            }
        }
    }


}

interface ICatsView {
    fun catsMessage(message: String?)
    fun <T> populate(param: T)
//    fun catsImage(image: CatsImage)
}