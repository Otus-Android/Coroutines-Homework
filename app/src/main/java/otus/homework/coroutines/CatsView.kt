package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter: CatsPresenter? = null
    var viewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//            presenter?.onInitComplete()
            viewModel?.loadFact()
        }
    }

    override fun populate(factAndImage: FactImageUI) {
        findViewById<TextView>(R.id.fact_textView).text = factAndImage.text
        val imgView = findViewById<ImageView>(R.id.image)
        Picasso.get()
            .load(factAndImage.url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .resize(300, 400)
            .centerCrop()
            .into(imgView)
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun renderState(state: Result<FactImageUI>) {
        when (state) {
            is Result.Success -> {
                populate(
                    FactImageUI(
                        text = state.data.text,
                        url = state.data.url
                    )
                )
            }

            is Result.Error -> {
                if (state.exception is SocketTimeoutException) {
                    showToast("Не удалось получить ответ от сервера")
                } else {
                    showToast(state.exception.message.toString())
                    CrashMonitor.trackWarning()
                }
            }
        }
    }
}

interface ICatsView {

    fun populate(factAndImage: FactImageUI)

    fun showToast(text: String)

    fun renderState(state: Result<FactImageUI>)
}