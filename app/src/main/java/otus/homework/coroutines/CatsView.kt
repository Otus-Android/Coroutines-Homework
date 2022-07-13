package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.dto.CatData

class CatsView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

  override fun populate(catData: CatData) {
    findViewById<TextView>(R.id.fact_textView).text = catData.catFactText
    Picasso.get().load(catData.catImageUri).into(findViewById<ImageView>(R.id.cat_image))
  }

  override fun showToast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
  }
}

interface ICatsView {
  fun populate(catData: CatData)
  fun showToast(text: String)
}