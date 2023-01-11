package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
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
            presenter?.onInitComplete()
        }
    }

    override fun populate(state: CatsViewState) {
        findViewById<TextView>(R.id.fact_textView).text = state.fact.fact
        Picasso.get()
            .load(state.pictureUrl.pictureUrl)
            .into(findViewById<AppCompatImageView>(R.id.fact_picture))
    }

    override fun toast(error: DisplayError) {
        when(error){
            is DisplayError.Timeout -> {
                Toast.makeText(context, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT).show()
            }
            is DisplayError.Other -> {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

interface ICatsView {

    fun populate(state: CatsViewState)
    fun toast(error: DisplayError)

}