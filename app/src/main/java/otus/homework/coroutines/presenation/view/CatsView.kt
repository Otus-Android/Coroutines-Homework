package otus.homework.coroutines.presenation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.INetworkExceptionHandler
import otus.homework.coroutines.presenation.model.CatsCard
import otus.homework.coroutines.presenation.presenter.CatsPresenter

class CatsView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

	var presenter: CatsPresenter? = null

	var exceptionHandler: INetworkExceptionHandler? = null

	var onButtonClick: (() -> Unit)? = null

	override fun onFinishInflate() {
		super.onFinishInflate()
		findViewById<Button>(R.id.button).setOnClickListener {
			presenter?.getCatsFact()
			onButtonClick?.invoke()
		}
	}

	override fun populate(catsCard: CatsCard) {
		findViewById<TextView>(R.id.fact_textView).text = catsCard.factText
		val imageView = findViewById<ImageView>(R.id.fact_catImage)
		Picasso.Builder(context)
			.build()
			.load(catsCard.imageUrl)
			.placeholder(R.drawable.ic_image_placeholder)
			.error(R.drawable.ic_image_placeholder_error)
			.into(imageView);
	}

	override fun showError(throwable: Throwable) {
		exceptionHandler?.handleException(throwable)
	}
}

interface ICatsView {
	fun populate(catsCard: CatsCard)
	fun showError(throwable: Throwable)
}