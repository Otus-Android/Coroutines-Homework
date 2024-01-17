package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.databinding.ActivityMainBinding

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    lateinit var binding: ActivityMainBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ActivityMainBinding.bind(this)
        /*
        binding.button.setOnClickListener {
            presenter?.onInitComplete()
        }
        */
    }

    override fun populate(uiModel: CatsUiModel) {
        if (uiModel.fact == Fact.EMPTY) return
        binding.factTextView.text = uiModel.fact.fact
        Picasso.get().load(uiModel.image.url).fit().centerCrop()
            .placeholder(R.drawable.pic_loading)
            .error(R.drawable.no_image)
            .into(binding.catImageView)
    }

    override fun toast(messageId: Int, vararg args: Any?) {
        Toast.makeText(
            context, context.getString(messageId, *args), Toast.LENGTH_LONG
        ).show()
    }
}

interface ICatsView {

    fun populate(uiModel: CatsUiModel)

    fun toast(@StringRes messageId: Int, vararg args: Any?)
}
