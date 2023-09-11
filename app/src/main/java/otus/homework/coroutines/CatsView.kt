package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.Pic
import otus.homework.coroutines.network.Result
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var viewModel: CatsViewModel

    override fun onFinishInflate() {
        super.onFinishInflate()


        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    fun startObserve(viewModel: CatsViewModel) {
        this.viewModel = viewModel
        populateObserve()
    }

    private fun populateObserve() {
        val text = findViewById<TextView>(R.id.fact_textView)
        val pic = findViewById<ImageView>(R.id.pic_imageView)
        viewModel.resultDataValue.observe(context as MainActivity) { result ->
            when (result) {
                is Result.Error -> {
                    showError(result.exception)
                }

                is Result.Success -> {

                    when (result.data) {

                        is Fact -> {
                            text.text = result.data.fact
                        }

                        is Pic -> {
                            Picasso.get()
                                .load("https://cataas.com" + result.data.url)
                                .into(pic)
                        }

                    }

                }
            }
        }
    }


    private fun showError(exception: Exception) {
        val errorMessage = when (exception) {
            is SocketTimeoutException -> {
                "Не удалось получить ответ от сервера"
            }

            else -> {
                CrashMonitor.trackWarning(exception)
                exception.message.toString()

            }
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}


interface ICatsView {

    fun populate(data: PopulateData)

    fun error(errorText: String)
}