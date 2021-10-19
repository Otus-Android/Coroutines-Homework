package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.lang.Exception

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

    override fun populateForViewModel(result: Result) {
        when (result) {
            is Result.Error -> callOnErrorSocketException()
            is Result.Success<*, *> -> {
                populate(FullFact(result.data as Fact,result.data2 as ImageFact))
            }
        }
    }

    override fun populate(fullFact: FullFact) {
        findViewById<TextView>(R.id.fact_textView).text = fullFact.fact.text
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get()
            .load(fullFact.factImg.file)
            .placeholder(R.drawable.ic_baseline_360_24)
            .error(R.drawable.ic_baseline_error_24)
            .into(imageView);
    }

    override fun callOnErrorSocketException() {
        Toast.makeText(context, "Не удалось получить ответ от сервером", Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {
    fun populateForViewModel(result: Result)
    fun populate(fullFact: FullFact)
    fun callOnErrorSocketException()
}