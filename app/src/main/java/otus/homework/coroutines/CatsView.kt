package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.domain.AnimalCard
import otus.homework.coroutines.network.Fact

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

    override fun populate(animalCard: AnimalCard) {
        findViewById<TextView>(R.id.fact_textView).text = animalCard.text
        val imageView = findViewById<ImageView>(R.id.image)
        Picasso.get().load(animalCard.imageUrl).into(imageView)
    }

    override fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(animalCard: AnimalCard)
    fun showError(msg: String)
}