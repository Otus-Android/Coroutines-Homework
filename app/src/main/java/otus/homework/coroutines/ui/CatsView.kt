package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Error
import otus.homework.coroutines.model.Loading
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.Success
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.ICatsView

/**
 * Кастомная View для отображения фактов и картинок.
 */
class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    private var factTextView: TextView? = null
    private var imageView: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var button: Button? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        factTextView = findViewById(R.id.fact_textView)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        button = findViewById(R.id.button)

        button?.setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    /**
     * Заполнение данными.
     * Согласно пункту ТЗ принимает один объект (Result),
     * содержащий все необходимые данные или ошибку.
     * Использует размеры изображения для установки AspectRatio.
     */
    override fun populate(result: Result<CatsData>) {
        when (result) {
            is Loading -> {
                showLoading(true)
            }

            is Success -> {
                showLoading(false)
                val data = result.data
                factTextView?.text = data.fact.text

                // Устанавливаем AspectRatio на основе размеров картинки
                imageView?.let { view ->
                    val params = view.layoutParams as LayoutParams
                    params.dimensionRatio = "${data.image.width}:${data.image.height}"
                    view.layoutParams = params
                }

                Picasso.get()
                    .load(data.image.url)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_close_clear_cancel)
                    .into(imageView)
            }

            is Error -> {
                showLoading(false)
                showToast(result.message)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar?.visibility = if (isLoading) VISIBLE else GONE
        button?.isEnabled = !isLoading
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
