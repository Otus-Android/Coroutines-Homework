package otus.homework.coroutines.catsfeature

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val picasso = Picasso.Builder(context).build()
    private lateinit var catFactTextView: TextView
    private lateinit var catImageImageView: ImageView

    var onRefresh: () -> Unit = {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            onRefresh.invoke()
        }
        catFactTextView = findViewById(R.id.fact_textView)
        catImageImageView = findViewById(R.id.cat_image)
    }

    fun populate(catInfo: CatInfo) {
        catFactTextView.text = catInfo.fact
        picasso
            .load(catInfo.imageUrl)
            .into(catImageImageView)
    }
}