package otus.homework.coroutines.presentation.utlis

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.viewbinding.ViewBinding

fun <T: ViewBinding> FragmentActivity.viewBinding(
    inflating: (LayoutInflater) -> T,
): Lazy<T> {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        inflating(layoutInflater).apply {
            root.setViewTreeViewModelStoreOwner(this@viewBinding)
            root.setViewTreeLifecycleOwner(this@viewBinding)
        }
    }
}