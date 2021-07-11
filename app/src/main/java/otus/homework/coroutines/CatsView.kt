package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var viewModel: MainActivityViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
//           Реализация Презентора--------------------
//            presenter?.onInitComplete()
//            ----------------------------------------
//            Реализация ViewModel--------------------
            viewModel?.onInitComplete()
//            ----------------------------------------
        }
    }

    override fun populate(data: CustomCatPresentationModel) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact

        Picasso.get()
            .load(data.imagePath)
            .resize(800, 800)
            .centerCrop()
            .into(findViewById(R.id.picture_imageView), object : Callback {
                override fun onSuccess() {}
                override fun onError(exception: Exception) {
                    exception.message?.let { CrashMonitor.trackWarning(it) }
                }
            })
    }

    override fun showErrorToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}

interface ICatsView {
    fun populate(data: CustomCatPresentationModel)
    fun showErrorToast(message: String)
}