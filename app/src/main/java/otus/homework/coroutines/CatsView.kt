package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var refreshFun: Refresh? = null
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById<TextView>(R.id.fact_textView)
        imageView = findViewById<ImageView>(R.id.image)
        findViewById<Button>(R.id.button).setOnClickListener {
            refreshFun?.call()
        }
    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Error -> showError(result)
            is Result.Loading -> showLoading(result)
            is Result.Success -> showSuccess(result)
        }

    }

    private fun showError(error: Result.Error) {
        imageView.visibility = GONE
        textView.text = error.err
    }

    private fun showLoading(loading: Result.Loading) {
        imageView.visibility = GONE
        textView.text = context.getString(R.string.loading)
    }

    private fun showSuccess(success: Result.Success) {
        var text: String? = success.catFact
        text ?: run { text = "No data" }
        textView.text = text
        imageView.visibility = VISIBLE
        Picasso
            .get()
            .load(success.imagePath)
            .resize(0, height)
            .into(imageView)
    }
}

interface ICatsView {
    fun populate(result: Result)
}


interface Refresh {
    fun call()
}