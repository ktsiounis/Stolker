package com.example.stolker.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Product
import com.example.stolker.R
import com.example.stolker.databinding.FragmentProductsBinding
import com.example.stolker.ui.adapters.ProductsAdapter
import com.example.stolker.ui.viewmodels.MainViewModel
import com.example.stolker.ui.viewmodels.ProductsUiAction
import com.example.stolker.ui.viewmodels.ProductsUiState
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
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

        binding.searchManuallyFab.setOnClickListener {
            showManualProductSearchDialog()
        }

        viewModel.getProducts()

        with(binding.productsRecyclerView) {
            adapter = ProductsAdapter(arrayListOf()) {
                navigateToProductDetails(it)
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
                                binding.textviewNoAvailableProducts.visibility = View.GONE
                                productsAdapter.updateProducts(uiState.products)
                            }
                            is ProductsUiState.Error -> {
                                Snackbar.make(
                                    view,
                                    uiState.message,
                                    LENGTH_LONG
                                ).show()
                            }
                            ProductsUiState.NotAvailableProducts -> {
                                binding.textviewNoAvailableProducts.visibility = View.VISIBLE
                            }
                        }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiAction
                    .collect { uiAction ->
                        when(uiAction) {
                            is ProductsUiAction.NavigateToProductDetails -> navigateToProductDetails(uiAction.product)
                        }
                    }
            }
        }

    }

    private fun showManualProductSearchDialog() {
        val manualProductSearchCustomView = layoutInflater.inflate(R.layout.manual_product_search_dialog_layout, null)
        val manualProductSearchEditText: EditText = manualProductSearchCustomView.findViewById(R.id.manual_product_id_edittext)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.manual_product_search_dialog_title))
            .setView(manualProductSearchCustomView)
            .setPositiveButton(getString(R.string.search_button_text)) { dialogInterface, _ ->
                viewModel.getProductDetails(manualProductSearchEditText.text.toString())
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    private fun navigateToProductDetails(product: Product) {
        val bundle = bundleOf(ProductDetailsFragment.PRODUCT_BUNDLE_KEY to product)
        findNavController().navigate(R.id.action_ProductsFragment_to_ProductDetailsFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}