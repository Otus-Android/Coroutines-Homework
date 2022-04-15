package otus.homework.coroutines

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import otus.homework.coroutines.uientities.UiFactEntity

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel: CatsViewModel? = null

    private var button: Button? = null
    private var tvFact: TextView? = null
    private var ivCatImage: AppCompatImageView? = null
    private var progressBar: ProgressBar? = null

    private val imageLoadingCallback: ImageLoadingCallback? by lazy {
        progressBar?.let { ImageLoadingCallback(it) }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvFact = findViewById<TextView>(R.id.tv_fact)?.apply {
            movementMethod = ScrollingMovementMethod()
        }

        ivCatImage = findViewById(R.id.iv_cat_image)
        progressBar = findViewById(R.id.progress_bar)

        button = findViewById<Button>(R.id.btn_more_facts).apply {
            setOnClickListener {
                progressBar?.visibility = View.VISIBLE
                ivCatImage?.setImageDrawable(null)
                tvFact?.text = ""

                viewModel?.onInitComplete()
            }
        }
    }

    override fun render(result: Result<UiFactEntity>) {
        when (result) {
            is ServerError -> {
                showServerError()
            }
            is OtherError -> {
                val errorMessage = result.errorMessage
                    ?: context.getString(R.string.default_error_message)
                showDefaultError(message = errorMessage)
            }
            is Success<UiFactEntity> -> {
                populate(result.result)
            }
        }
    }

    private fun populate(uiFactEntity: UiFactEntity) {
        tvFact?.text = uiFactEntity.fact

        Picasso.get()
            .load(uiFactEntity.imageUrl)
            .resize(256, 256)
            .centerCrop()
            .into(ivCatImage, imageLoadingCallback)
    }

    private fun showServerError() {
        progressBar?.visibility = View.GONE

        val serverErrorMessage = context.getString(R.string.server_error_message)
        val toast = Toast.makeText(context, serverErrorMessage, Toast.LENGTH_SHORT)

        toast.show()
    }

    private fun showDefaultError(message: String) {
        progressBar?.visibility = View.GONE

        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)

        toast.show()
    }

    class ImageLoadingCallback(
        private val progressBar: ProgressBar
    ) : Callback {

        override fun onSuccess() {
            progressBar.visibility = View.GONE
        }

        override fun onError(e: Exception?) {
            progressBar.visibility = View.GONE
        }
    }
}

interface ICatsView {
    fun render(result: Result<UiFactEntity>)
}