package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.CatViewData

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private lateinit var image: ImageView
    private lateinit var text: TextView
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    override fun onFinishInflate() {
        super.onFinishInflate()

        image = findViewById(R.id.fact_imageView)
        text = findViewById(R.id.fact_textView)
        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.fact_progressBar)
    }

    override fun setOnButtonClick(onButtonClick: () -> Unit) {
        button.setOnClickListener { onButtonClick() }
    }

    override fun populate(data: CatViewData) {
        text.text = data.fact

        Picasso.get()
            .load(data.imageUrl)
            .into(image)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(data: CatViewData)
    fun showError(message: String)
    fun setOnButtonClick(onButtonClick: () -> Unit)
}
