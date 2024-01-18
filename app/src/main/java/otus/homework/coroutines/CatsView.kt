package otus.homework.coroutines

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        findViewById<Button>(R.id.button).setOnClickListener {
            (context as MainActivity).getCats()
           // presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact,img: Img) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        val catImg = findViewById<ImageView>(R.id.img_ImageView)
        Picasso.get().load(img.url).into(catImg)
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }


}

interface ICatsView {

    fun populate(fact: Fact,img: Img)
    fun showError(error: String)

}
