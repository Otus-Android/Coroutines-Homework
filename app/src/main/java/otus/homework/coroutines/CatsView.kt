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

    var presenter: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(state: CatsScreenState) {
        when (state) {
            is CatsScreenState.Success<*> -> {
                (state.data as? CatInfo)?.let { catInfo ->
                    findViewById<TextView>(R.id.fact_textView).text = catInfo.catFact
                    Picasso.get().load(catInfo.catImageUrl).into(findViewById<ImageView>(R.id.image))
                }
            }

            is CatsScreenState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}

interface ICatsView {
    fun populate(state: CatsScreenState)
}