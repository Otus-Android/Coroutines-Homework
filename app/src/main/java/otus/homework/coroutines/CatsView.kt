package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.CatImage
import otus.homework.coroutines.domain.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {


    var callback: (() -> Unit)? = null
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        button = findViewById(R.id.button)
        button.setOnClickListener { callback?.invoke() }
        textView = findViewById(R.id.fact_textView)
        imageView = findViewById(R.id.cat_image_view)
    }

    override fun populate(data: Any) {
        if (data is Fact) textView.text = data.fact
        else if (data is CatImage) {
            Picasso.get()
                .load(data.url)
                .error(R.drawable.baseline_image_not_supported_24)
                .centerCrop()
                .fit()
                .into(imageView)
        }

    }
}
