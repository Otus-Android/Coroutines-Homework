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
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var viewModel: CatsViewModel

    override fun onFinishInflate() {
        super.onFinishInflate()


        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
    }

   fun startObserve(viewModel: CatsViewModel){
       populateObserve()
       errorObserve()
   }

    private fun populateObserve() {
        val text = findViewById<TextView>(R.id.fact_textView)
        val pic = findViewById<ImageView>(R.id.pic_imageView)
        viewModel.populateDataValue?.observe(context as MainActivity) { data ->
            text.text = data.factText
            Picasso.get()
                .load("https://cataas.com" + data.imageCat)
                .into(pic)
        }
    }

    private fun errorObserve() {
        viewModel.errorDataValue?.observe(context as MainActivity) { errorText ->
            Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
        }

    }
}

interface ICatsView {

    fun populate(data: PopulateData)

    fun error(errorText: String)
}