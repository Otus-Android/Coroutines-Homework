package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.databinding.CatsViewBinding

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {
    private val binding: CatsViewBinding by lazy {
        CatsViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.button.setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact) {
        binding.factTextView.text = fact.text
    }
}

interface ICatsView {
    fun populate(fact: Fact)
}
