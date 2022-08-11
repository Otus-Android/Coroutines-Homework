package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var catsViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel?.onInitComplete()
        }
    }

    private val myObserver = Observer<Result<Exception, Cat>> { result ->
        when (result) {
            is Success -> {
                findViewById<TextView>(R.id.fact_textView).text = result.value.text
                Picasso.get().load(result.value.image).into(findViewById<ImageView>(R.id.catImage))
            }
            is Error -> {
                val message = when (result.value) {
                    is SocketTimeoutException -> "Не удалось получить ответ от сервером"
                    else -> {
                        result.value.message ?: "Ошибка"
                    }
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        catsViewModel?.resultData?.observeForever(myObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        catsViewModel?.resultData?.removeObserver(myObserver)
    }
}
