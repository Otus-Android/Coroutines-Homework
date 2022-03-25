package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.ImgResource
import com.squareup.picasso.Picasso
import otus.homework.coroutines.presenter.CatsPresenter


class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    lateinit var progressBar: ProgressBar


    override fun onFinishInflate() {
        super.onFinishInflate()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        findViewById<Button>(R.id.button_refresh).setOnClickListener {
            presenter?.onInitComplete()
        }
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            presenter?.onStopCoroutine()
        }
    }

    override fun populate(fact: Fact, imageResource: ImgResource) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(imageResource.fileUrl).into(findViewById<ImageView>(R.id.fact_img))
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE;
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}


interface ICatsView {
    fun showToast(message: String)
    fun populate(fact: Fact, imageResource: ImgResource)
    fun showLoading()
    fun hideLoading()
}