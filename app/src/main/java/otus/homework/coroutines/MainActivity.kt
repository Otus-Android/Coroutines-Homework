package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity(), CatsPresenter.ToastEventListener {

    //lateinit var catsPresenter: CatsPresenter
    //private val diContainer = DiContainer()
    private val catsVm: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsVm.fetchData()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            catsVm.fetchData()
        }
        catsVm.livePresentationModel.observe(this) {
            when (it) {
                is Result.Success<*> -> view.populate(it.successBody as CatPresentationModel)
                is Result.Error -> onToastEvent(it.message)
            }
        }
        //catsPresenter = CatsPresenter(diContainer.service, this)
        //view.presenter = catsPresenter
        //catsPresenter.attachView(view)
        //catsPresenter.onInitComplete()


    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
        }
        super.onStop()
    }

    override fun onToastEvent(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}