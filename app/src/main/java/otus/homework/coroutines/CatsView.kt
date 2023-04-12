package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact
        Picasso.get().load(catData.picLink).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
    override fun onInflate(mainViewModel: MainViewModel) {
        findViewById<Button>(R.id.button).setOnClickListener {
            mainViewModel.getCatData()
        }
    }
}

interface ICatsView {

    fun populate(catData: CatData)

    fun showToast(text: String)

    fun onInflate (mainViewModel: MainViewModel)

}