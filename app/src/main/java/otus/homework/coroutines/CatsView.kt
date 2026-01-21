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

    private lateinit var factTextView: TextView
    private lateinit var catImageView: ImageView
    private lateinit var button: Button

    override fun onFinishInflate() {
        super.onFinishInflate()

        factTextView = findViewById(R.id.fact_textView)
        catImageView = findViewById(R.id.cat_imageView)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(data: CatPresentationModel) {
        factTextView.text = data.fact
        Picasso.get()
            .load(data.imageUrl)
            .into(catImageView)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(data: CatPresentationModel)
    fun showToast(message: String)
}