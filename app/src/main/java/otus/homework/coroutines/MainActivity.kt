package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer: DiContainer = DiContainer()
    private val diContainerPicture: DiContainerPicture = DiContainerPicture()
    private lateinit var view: CatsView
    private val viewModelFactory by lazy {
        MyViewModel.MyViewModelFactory(diContainer.service, diContainerPicture.servicePicture)
    }
    private val myViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        //вью модель
        myViewModel.factAndPicture?.observe(this, Observer {
            it?.let {
                handlerResult(it)
                view.findViewById<Button>(R.id.button).isEnabled = true
            }
        })

        view.myViewModel = myViewModel
        myViewModel.onInitComplete()
        view.findViewById<Button>(R.id.button).isEnabled = false

        //презентер
        catsPresenter = CatsPresenter(diContainer.service, diContainerPicture.servicePicture)

        catsPresenter.factAndPicture?.observe(this, Observer {
            it?.let {
                handlerResult(it)
                view.findViewById<Button>(R.id.button).isEnabled = true
            }
        })

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    fun handlerResult(result: Result<Any>) {
        when(result){
            is Result.Error -> {Toast.makeText(this, result.exception, Toast.LENGTH_LONG).show()}
            is Result.Success<Any> -> {view.populate(result.data as FactAndPicture)}
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}