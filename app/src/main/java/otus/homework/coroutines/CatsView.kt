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
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel : CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            findViewById<ProgressBar>(R.id.catsImageLoader).visibility = View.VISIBLE
            findViewById<TextView>(R.id.fact_textView).text = ""
            findViewById<ImageView>(R.id.catsImage).visibility = View.INVISIBLE
            viewModel?.onInitComplete()
        }
    }

    override fun populate(catsStuff: Result<CatsData>) {
        when(catsStuff) {
            is Result.Success -> render(catsStuff.data)
            is Result.Error -> {
                inCaseOfError(catsStuff.msg)
            }
        }
    }

    override fun showShortToast(toastyMsg: String?) {
        Toast.makeText(context, toastyMsg, Toast.LENGTH_SHORT).show()
    }

    override fun stopPictureLoading() {
        Picasso.get().cancelTag(PICASSO_LOADING_TAG)
    }

    override fun inCaseOfError(msg: String?) {
        showShortToast(msg)
        findViewById<ImageView>(R.id.catsImage).apply {
            setImageDrawable(
                ResourcesCompat
                    .getDrawable(
                        resources,
                        R.drawable.ic_baseline_error_outline_24,
                        context.theme
                    )
            )
            visibility = View.VISIBLE
        }
        findViewById<ProgressBar>(R.id.catsImageLoader).visibility = View.GONE
    }

    private fun render(catsData: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsData.textFact
        val image = findViewById<ImageView>(R.id.catsImage)
        val imageLoader = findViewById<ProgressBar>(R.id.catsImageLoader)
        Picasso.get()
            .load(catsData.imageUrl)
            .tag(PICASSO_LOADING_TAG)
            .into(image, object: Callback {
                override fun onSuccess() {
                    image.visibility = View.VISIBLE
                    imageLoader.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    inCaseOfError(e?.message)
                }
            })
    }

    companion object {
        private const val PICASSO_LOADING_TAG = "picasso_loading_tag"
    }
}

interface ICatsView {

    fun populate(catsStuff: Result<CatsData>)

    fun showShortToast(toastyMsg: String?)

    fun stopPictureLoading()

    fun inCaseOfError(msg: String?)
}