package otus.homework.coroutines

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import otus.homework.coroutines.model.Fact
import java.net.URL
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.Result.*

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: ICatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.doReadThings()
        }
    }

    override suspend fun populateFact(fact: String) {
        fact.let { findViewById<TextView>(R.id.fact_textView).text = it }
    }

    override suspend fun populateImage(image: Bitmap) {
        findViewById<ImageView>(R.id.imageView).setImageBitmap(image)
    }

    override suspend fun populateResult(result: Result) {
        when (result) {
            is SuccessFact -> {
                populateFact(result.fact.text)
            }
            is SuccessImage -> {
                populateImage(result.image)
            }
            is Error -> {
                showToast(result.reason)
            }
        }
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    suspend fun populateFact(fact: String)
    suspend fun populateImage(image: Bitmap)
    suspend fun populateResult(result: Result)
    fun showToast(text: String)
}