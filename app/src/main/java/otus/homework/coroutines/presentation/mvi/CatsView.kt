package otus.homework.coroutines.presentation.mvi

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.squareup.picasso.Picasso
import io.reactivex.functions.Consumer
import otus.homework.coroutines.R
import otus.homework.coroutines.presentation.mvi.components.CatsViewBindings
import otus.homework.coroutines.presentation.mvi.components.Feature
import otus.homework.coroutines.presentation.mvi.components.NewsConsumer
import otus.homework.coroutines.presentation.mvi.models.State
import otus.homework.coroutines.presentation.mvi.models.Wish
import otus.homework.coroutines.utils.CustomApplication

/**
 * `Custom view` с информацией о случайном коте.
 *
 * Построено на основе паттерна `MVI` с использованием `MVICore`.
 * Отличительная особенность: используются ближайший компонентов вверх по иерархии,
 * являющийся [LifecycleOwner]. В случае отсутствия такого будет сгенерировано
 * исключение [IllegalStateException]
 */
class CatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), Consumer<State> {

    private val diContainer = CustomApplication.diContainer(context)

    private val picasso: Picasso = diContainer.picasso

    private val feature by lazy(LazyThreadSafetyMode.NONE) {
        with(diContainer) { Feature(catRepository, stringProvider) }
    }

    private val mviBindings by lazy(LazyThreadSafetyMode.NONE) {
        CatsViewBindings(
            lifecycleOwner = checkNotNull(findViewTreeLifecycleOwner()) { "there must be an owner" },
            feature = feature,
            newsConsumer = NewsConsumer(context)
        )
    }

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        context
        textView = findViewById(R.id.fact_text_view)
        imageView = findViewById(R.id.image_view)
        findViewById<Button>(R.id.button).setOnClickListener {
            feature.accept(wish = Wish.LoadCat)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        mviBindings.setup(this)
        feature.accept(wish = Wish.LoadCat)
    }

    override fun accept(state: State) = when (state) {
        State.Idle -> onIdle()
        is State.Success -> onSuccess(state)
    }

    private fun onIdle() {
        picasso.cancelRequest(imageView)
        imageView.setImageDrawable(null)
    }

    private fun onSuccess(state: State.Success) {
        textView.text = state.cat.fact
        picasso.load(state.cat.image).placeholder(R.drawable.question_mark).into(imageView)
    }
}