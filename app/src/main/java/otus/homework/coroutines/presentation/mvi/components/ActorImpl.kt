package otus.homework.coroutines.presentation.mvi.components

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.rx2.rxSingle
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.presentation.mvi.models.Effect
import otus.homework.coroutines.presentation.mvi.models.State
import otus.homework.coroutines.presentation.mvi.models.Wish
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import java.net.SocketTimeoutException


/**
 * Производитель внутренних изменений [Effect]
 *
 * @param repository репозиторий информации о кошке
 * @param stringProvider поставщик строковых значений
 */
class ActorImpl(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> = when (wish) {
        Wish.LoadCat -> rxSingle { repository.getCatInfo() }
            .map { cat -> Effect.SuccessfulLoaded(cat) as Effect }
            .toObservable()
            .startWith(Observable.just(Effect.StartedLoading))
            .doOnError { e -> CrashMonitor.trackWarning(e) }
            .onErrorReturn { e -> Effect.ErrorLoading(geErrorMessage(e)) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun geErrorMessage(e: Throwable) = if (e is SocketTimeoutException) {
        stringProvider.getString(R.string.timeout_server_error)
    } else {
        stringProvider.getString(
            R.string.unexpected_request_error, e.messageOrDefault()
        )
    }

    private fun Throwable.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)
}