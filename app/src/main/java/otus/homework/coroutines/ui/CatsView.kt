package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

    //var presenter: CatsPresenter? = null

    override fun setOnButtonClickListener(onClickListener: OnClickListener) {
        findViewById<Button>(R.id.button).setOnClickListener(onClickListener)
    }

    override fun populate(cat: CatUi) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        val catImage = findViewById<ImageView>(R.id.cat_image)
        Picasso.get().load(cat.imageUrl).into(catImage)
    }

    override fun showToastDefaultFailed(throwable: Throwable) {
        val text = if (throwable is SocketTimeoutException) {
            context.getString(R.string.failedToResponse)
        } else {
            throwable.message
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populate(cat: CatUi)
    fun setOnButtonClickListener(onClickListener: View.OnClickListener)
    fun showToastDefaultFailed(throwable: Throwable)
}