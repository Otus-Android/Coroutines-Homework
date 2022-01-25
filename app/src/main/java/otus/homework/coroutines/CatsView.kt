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

    //var presenter :CatsPresenter? = null
    var viewModel :CatsViewModel? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()

        }
    }

    override fun populate(fact: Fact, picture: Picture) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso
            .get()
            .load(picture.file)
            .placeholder(R.drawable.pngwing)
            .error(R.drawable.pngwing)
            .into(findViewById<ImageView>(R.id.imageView))
    }
    override fun  plant(catModel: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = catModel.fact.text
        Picasso
            .get()
            .load(catModel.picture.file)
            .placeholder(R.drawable.pngwing)
            .error(R.drawable.pngwing)
            .into(findViewById<ImageView>(R.id.imageView))
        }


    override fun toast(text:String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {
    fun  plant (catModel: CatModel)
    fun populate(fact: Fact, picture: Picture)
    fun toast(text: String)
}