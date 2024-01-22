package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

  var presenter: CatsPresenter? = null

  override fun onFinishInflate() {
    super.onFinishInflate()
    findViewById<Button>(R.id.button).setOnClickListener { presenter?.onInitComplete() }
  }

  override fun populate(catInfo: CatInfo) {
    findViewById<TextView>(R.id.fact_textView).text = catInfo.fact
    val imageView = findViewById<ImageView>(R.id.avatar_imageView)

    Picasso.get()
        .load(catInfo.images.first().url)
        .resize(catInfo.images.first().width, catInfo.images.first().height)
        .into(imageView)
  }

  override fun showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
  }
}

interface ICatsView {

  fun populate(catInfo: CatInfo)

  fun showToast(message: String)
}
