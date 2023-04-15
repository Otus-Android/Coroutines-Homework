package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatInfoDto

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private lateinit var text: TextView
    private lateinit var progress: ProgressBar
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            progress.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            presenter?.onInitComplete()
        }
    }

    override fun populate(catInfo: CatInfoDto) {
        text = findViewById(R.id.fact_textView)
        progress = findViewById(R.id.progress)
        imageView = findViewById(R.id.imageView)
        text.text = catInfo.text
        Picasso.get()
            .load(catInfo.imageUrl)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    progress.visibility = View.GONE
                    imageView.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    progress.visibility = View.GONE
                }
            })
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catInfo: CatInfoDto)
    fun showToast(message: String)
}