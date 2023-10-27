package otus.homework.coroutines.catsfeature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.databinding.FragmentCatsBinding

class CatsFragment : Fragment() {

    private val diContainer = DiContainer(debug = true)
    private val viewModel: CatsViewModel by viewModels { CatsViewModel.provideFactory(diContainer) }
    private var _binding: FragmentCatsBinding? = null
    private val binding: FragmentCatsBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.onRefresh = {
            viewModel.dispatch(CatsAction.Ui.Refresh)
        }

        viewModel.state
            .onEach { state ->
                if (state.data != null) binding.root.populate(state.data)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.events
            .onEach { event ->
                when (event) {
                    is CatsEvent.Error -> showError(event.cause)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        Toast
            .makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance(): CatsFragment = CatsFragment()
    }
}