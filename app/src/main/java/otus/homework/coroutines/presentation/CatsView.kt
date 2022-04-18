package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.data.CatDto

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    lateinit var viewModel: CatsViewModel

    lateinit var loaderView: FrameLayout

    override fun onFinishInflate() {
        super.onFinishInflate()

        loaderView = findViewById(R.id.loader)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.loadCatFactWithImage()
        }
    }

    override fun displayError(error: CatsError) {
        loaderView.visibility = GONE
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun displayData(catDto: CatDto) {
        loaderView.visibility = GONE
        findViewById<TextView>(R.id.fact_textView).text = catDto.fact.text
        Picasso.get()
            .load(catDto.photoUrl)
            .into(findViewById<ImageView>(R.id.catPhoto))
    }

    override fun displayLoading() {
        loaderView.visibility = VISIBLE
    }
}

interface ICatsView {
    fun displayError(error: CatsError)
    fun displayData(catDto: CatDto)
    fun displayLoading()
}