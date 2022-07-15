package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsVM: CatsVm by viewModels {
        CatsVm.CatsVmProviderFactory(diContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.vm = catsVM

        catsVM.showToast.observe(this) { text ->
            text.takeIf { it.isNotEmpty() }?.let {
                view.showToast(it)
                catsVM.resetToast()
            }
        }

        catsVM.catsData.observe(this) { catsModel ->
            catsModel?.let {
                view.populate(it)
            }
        }
    }
}