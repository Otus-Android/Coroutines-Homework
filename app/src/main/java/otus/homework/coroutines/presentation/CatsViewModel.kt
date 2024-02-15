package otus.homework.coroutines.presentation

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CrashMonitor
import otus.homework.coroutines.entity.CatData
import otus.homework.coroutines.data.server.CatsPhotosService
import otus.homework.coroutines.data.server.CatsTextService
import otus.homework.coroutines.di.DaggerComponent
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject

class CatsViewModel : ViewModel() {

    @Inject
    lateinit var catsService: CatsTextService

    @Inject
    lateinit var catsPhotosService: CatsPhotosService

    private val _state = MutableSharedFlow<State>()
    val state: SharedFlow<State>
        get() = _state

    init {
        DaggerComponent.create().inject(this)
    }

    fun onInitComplete() {
        viewModelScope.launch {
            try {
                val res1 = async { catsService.getCatFact() }
                val res2 = async { catsPhotosService.getCatPhoto() }
                val fact = res1.await()
                val photo = res2.await().getOrNull(0)

                _state.emit(ShowPhotoState(CatData(fact, photo)))
            } catch (e: SocketTimeoutException) {
                _state.emit(ErrorState(e.toString()))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.emit(ErrorState(e.toString()))
                CrashMonitor.trackWarning()
            }
        }
    }

    fun setUrlPhotoInView(imageView: ImageView, photoUrl: String) {
        Picasso.get().load(photoUrl).into(imageView)
    }
}