package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    // var presenter: CatsPresenter? = null

    var viewModel: CatsViewModel? = null

    private var imageView: ImageView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        imageView = findViewById<ImageView>(R.id.imageView)
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
            // presenter?.onInitComplete()
            Log.d("catsView", CoroutineName.toString())

        }
    }

    override fun populate(fact: Fact, image: Image) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        if (image.url.isNotEmpty()) {
            Picasso.get().load(image.url)
                .resize(width, width)
                .centerCrop()
                .into(imageView)
        }
    }

    override fun toastError() {
        Toast.makeText(context, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

}

interface ICatsView {

    fun populate(fact: Fact, image: Image)

    fun toastError()

}