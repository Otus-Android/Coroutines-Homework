package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.di.components.DaggerMainActivityComponent
import otus.homework.coroutines.di.components.MainActivityComponent
import otus.homework.coroutines.presentation.utlis.ComponentHolder
import otus.homework.coroutines.presentation.utlis.applicationComponent
import otus.homework.coroutines.presentation.utlis.viewBinding

class MainActivity : AppCompatActivity(), ComponentHolder<MainActivityComponent> {

    override val component: MainActivityComponent by lazy {
        DaggerMainActivityComponent.factory().create(
            applicationComponent = applicationComponent,
            context = this,
        )
    }

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}