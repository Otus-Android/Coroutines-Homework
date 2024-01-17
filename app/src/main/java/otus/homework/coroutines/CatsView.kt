package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import otus.homework.coroutines.databinding.ActivityMainBinding

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    private lateinit var binding: ActivityMainBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ActivityMainBinding.bind(this)
        binding.button.setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(uiModel: CatsUiModel) {
        when (uiModel) {
            is CatsUiModel.Post -> {
                binding.factTextView.text = uiModel.fact.fact
                Picasso.get().load(uiModel.image.url).fit().centerCrop()
                    .placeholder(R.drawable.pic_loading)
                    .error(R.drawable.no_image)
                    .into(binding.catImageView)
            }
            is CatsUiModel.Toast -> {
                Toast.makeText(
                    context, context.getString(uiModel.messageId, uiModel.varargs),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

interface ICatsView {

    fun populate(uiModel: CatsUiModel)
}
