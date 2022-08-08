package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact, imageUri: Image) {
        val imageView: ImageView = findViewById(R.id.imageView)
        val factView: TextView = findViewById(R.id.fact_textView)
        factView.text = fact.text
        Picasso.get().load(imageUri.catImageUri).into(imageView, object : Callback {
            override fun onSuccess() {
                imageView.visibility = View.VISIBLE
                factView.visibility = View.VISIBLE
                showProgressBar(false)
            }

            override fun onError(e: java.lang.Exception?) {
                showProgressBar(false)
            }
        })
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar(needShow: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        if (needShow) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun hideViews() {
        val imageView: ImageView = findViewById(R.id.imageView)
        val factView: TextView = findViewById(R.id.fact_textView)
        imageView.visibility = View.GONE
        factView.visibility = View.GONE
    }
}

interface ICatsView {
    fun populate(fact: Fact, imageUri: Image)
    fun showToast(message: String)
    fun showProgressBar(needShow: Boolean)
    fun hideViews()
}