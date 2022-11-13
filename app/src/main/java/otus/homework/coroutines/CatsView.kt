package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

//    var presenter :CatsPresenter? = null
    var catsViewModel: CatsViewModel? = null
    var service: CatsService? = null
    var awsService: CatsService? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        val scope = PresenterScope()
        findViewById<Button>(R.id.button).setOnClickListener {
            scope.launch {
                if (service != null && awsService != null) {
                    catsViewModel?.onInitComplete(service!!, awsService!!)
                }
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun populate(catResultData: CatResultData) {
        findViewById<TextView>(R.id.fact_textView).text = catResultData.text
        Picasso.get().load(catResultData.fileUrl).into(findViewById<ImageView>(R.id.img))
    }
}

interface ICatsView {

    fun showToast(message: String)

    fun populate(catResultData: CatResultData)
}