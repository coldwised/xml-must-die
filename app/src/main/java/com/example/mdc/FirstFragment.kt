package com.example.mdc

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mdc.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private val viewModel: ImagesViewModel by viewModels()

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageListAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { list ->
                    adapter.submitList(list)
                }
        }
        val context = context
        binding.imagesGridView.adapter = adapter
        binding.imagesGridView.layoutManager = AdaptiveGridLayoutManager(context, 230) //GridLayoutManager(context, calculateNoOfColumns(context!!, 100f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}