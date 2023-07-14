package otus.homework.coroutines.presentation.presenter

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.utils.CustomApplication

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    private val picasso: Picasso = CustomApplication.diContainer(context).picasso

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(R.id.fact_text_view)
        imageView = findViewById(R.id.image_view)
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
            picasso.cancelRequest(imageView)
        }
    }

    override fun populate(cat: Cat) {
        textView.text = cat.fact
        picasso
            .load(cat.image)
            .placeholder(R.drawable.question_mark)
            .into(imageView)
    }

    override fun warn(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(cat: Cat)

    fun warn(message: String)
}