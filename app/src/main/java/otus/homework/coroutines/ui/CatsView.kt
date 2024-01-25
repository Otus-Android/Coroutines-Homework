package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun render(result: Result<CatsData>) {
        when(result) {
            is Result.Success -> {
                val data = result.data
                populate(data)
            }
            is Result.Error -> {
                val error = result.error
                showError(error)
            }
        }
    }

    private fun populate(data: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact
        Picasso.get().load(data.pictureUrl).into(findViewById<ImageView>(R.id.pic_imageView))
    }

    private fun showError(error: Throwable) {
        val errorText = if (error is SocketTimeoutException) {
            context.getString(R.string.network_error)
        } else {
            error.message
        }
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun render(result: Result<CatsData>)

}