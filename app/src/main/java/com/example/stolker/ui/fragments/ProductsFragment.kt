package com.example.stolker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stolker.R
import com.example.stolker.databinding.FragmentProductsBinding
import com.example.stolker.ui.adapters.ProductsAdapter
import com.example.stolker.ui.viewmodels.MainViewModel
import com.example.stolker.ui.viewmodels.ProductsUiState
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.productsRecyclerView) {
            adapter = ProductsAdapter(arrayListOf()) {
                val bundle = bundleOf("product" to it)
                findNavController().navigate(R.id.action_ProductsFragment_to_ProductDetailsFragment, bundle)
            }.also {
                productsAdapter = it
            }
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .collect { uiState ->
                        when(uiState) {
                            is ProductsUiState.Success -> {
                                productsAdapter.updateProducts(uiState.products)
                            }
                            is ProductsUiState.Error -> {
                                Snackbar.make(
                                    view, uiState.message,
                                    BaseTransientBottomBar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}