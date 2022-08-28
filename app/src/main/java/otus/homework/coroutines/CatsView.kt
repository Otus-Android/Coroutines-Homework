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
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            findViewById<ProgressBar>(R.id.catsImageLoader).visibility = View.VISIBLE
            presenter?.onInitComplete()
        }
    }

    override fun populate(catsStuff: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsStuff.textFact
        val image = findViewById<ImageView>(R.id.catsImage)
        val imageLoader = findViewById<ProgressBar>(R.id.catsImageLoader)
        Picasso.get()
            .load(catsStuff.imageUrl)
            .tag(PICASSO_LOADING_TAG)
            .error(R.drawable.ic_baseline_error_outline_24)
            .into(image, object: Callback {
                override fun onSuccess() {
                    imageLoader.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    imageLoader.visibility = View.GONE
                }
            }
        )
    }

    override fun showShortToast(toastyMsg: String) {
        Toast.makeText(context, toastyMsg, Toast.LENGTH_SHORT).show()
    }

    override fun stopPictureLoading() {
        Picasso.get().cancelTag(PICASSO_LOADING_TAG)
    }

    companion object {
        private val PICASSO_LOADING_TAG = "picasso_loading_tag"
    }
}

interface ICatsView {

    fun populate(fcatsStuff: CatsData)

    fun showShortToast(toastyMsg: String)

    fun stopPictureLoading()
}