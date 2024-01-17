package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
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

    override fun populate(fact: Fact) {
        binding.factTextView.text = fact.fact
    }

    override fun toast(@StringRes messageId: Int, vararg args: Any?) {
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            Toast.makeText(context, context.getString(messageId, *args), Toast.LENGTH_LONG).show()
        }
    }
}

interface ICatsView {

    fun populate(fact: Fact)

    fun toast(@StringRes messageId: Int, vararg args: Any?)
}
