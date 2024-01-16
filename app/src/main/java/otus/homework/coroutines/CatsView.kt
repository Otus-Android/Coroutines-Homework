package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    private val scope = PresenterScope()

    private var imageView: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        imageView = findViewById<ImageView>(R.id.imageView)
        findViewById<Button>(R.id.button).setOnClickListener {
            scope.launch {
                presenter?.onInitComplete()
                Log.d("catsView", "${CoroutineName.toString()}")
            }
        }
    }

    override fun populate(fact: Fact, image: Image) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact

            Picasso.get().load(image.url)
                .resize(width, width)
                .centerCrop()
                .into(imageView)

    }


    override fun onDetachedFromWindow() {
        scope.cancel("Stop Stop PresenterScope in CatsView")
        super.onDetachedFromWindow()
    }

}

interface ICatsView {

    fun populate(fact: Fact,image: Image )

}