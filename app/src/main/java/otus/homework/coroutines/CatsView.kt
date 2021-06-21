package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import otus.homework.coroutines.databinding.ActivityMainBinding
import java.lang.Exception

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    //    var presenter: CatsPresenter? = null
    var viewModel: CatsViewModel? = null
    var binding: ActivityMainBinding? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun initialize(
        viewBinding: ActivityMainBinding, /*catsPresenter: CatsPresenter*/
        catsViewModel: CatsViewModel
    ) {
//        presenter = catsPresenter
        viewModel = catsViewModel
        binding = viewBinding
        binding?.button?.setOnClickListener {
//            presenter?.requestFact()
            viewModel?.requestFact()
        }
    }

    override fun populate(new: CatNewPresentationModel) {
        val binding = binding ?: return

        with(binding) {
            catImageLoadingProgress.visibility = View.VISIBLE
            factTextView.text = new.factText
            Picasso.get()
                .load(new.imageSource)
                .error(R.drawable.ic_errored_image)
                .into(catImageView, object : Callback {
                    override fun onSuccess() {
                        catImageLoadingProgress.visibility = View.GONE
                    }

                    override fun onError(exception: Exception) {
                        catImageLoadingProgress.visibility = View.GONE
                        exception.message?.let { CrashMonitor.trackWarning(it) }
                    }
                })
        }
    }

    override fun showMessage(stringRes: Int) =
        Snackbar.make(this, stringRes, Snackbar.LENGTH_LONG).show()
}

interface ICatsView {
    fun populate(new: CatNewPresentationModel)

    fun showMessage(@StringRes stringRes: Int)
    fun initialize(viewBinding: ActivityMainBinding, /*catsPresenter: CatsPresenter*/catsViewModel: CatsViewModel)
}