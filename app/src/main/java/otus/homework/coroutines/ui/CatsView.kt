package otus.homework.coroutines.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.ui.model.FactVO
import otus.homework.coroutines.utils.gone
import otus.homework.coroutines.utils.shortToast
import otus.homework.coroutines.utils.show

class CatsView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

  var presenter: CatsPresenter? = null

  lateinit var button: Button

  override fun onFinishInflate() {
    super.onFinishInflate()

    button = findViewById(R.id.button)

    button.setOnClickListener {
      presenter?.onInitComplete()
    }
  }

  override fun populate(fact: Fact) {
    findViewById<TextView>(R.id.fact_textView).text = fact.fact
  }

  override fun showLoading() {
    findViewById<ProgressBar>(R.id.fact_progressBar).show()
  }

  override fun hideLoading() {
    findViewById<ProgressBar>(R.id.fact_progressBar).gone()
  }

  override fun populate(factVO: FactVO) {
    findViewById<TextView>(R.id.fact_textView).text = factVO.fact
    Picasso
      .get()
      .load(factVO.imageUrl)
      .into(findViewById<ImageView>(R.id.fact_imageView))
  }

  override fun respondOnError(errorMsg: String) {
    context.shortToast(errorMsg)

    findViewById<TextView>(R.id.fact_textView).text = errorMsg
  }
}

interface ICatsView {
  fun populate(fact: Fact)
  fun populate(factVO: FactVO)
  fun showLoading()
  fun hideLoading()
  fun respondOnError(errorMsg: String)
}