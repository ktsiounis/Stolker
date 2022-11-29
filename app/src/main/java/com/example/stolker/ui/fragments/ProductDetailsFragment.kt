package com.example.stolker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.models.Product
import com.example.stolker.R
import com.example.stolker.databinding.FragmentProductDetailsBinding
import com.example.stolker.ui.utils.ConnectionState
import com.example.stolker.ui.utils.observeConnectivity
import com.example.stolker.ui.utils.percentageDifferenceWith
import com.example.stolker.ui.viewmodels.MainViewModel
import com.example.stolker.ui.viewmodels.ProductDetailsUiState
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()
    private val product: Product? by lazy {
        arguments?.getSerializable(PRODUCT_BUNDLE_KEY) as? Product
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product?.let { product ->

            renderProduct(product)

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.startSocketForProduct(product.id)
                    context
                        ?.observeConnectivity()
                        ?.drop(1)
                        ?.collect { connectionState ->
                            when(connectionState) {
                                ConnectionState.Available -> {
                                    showConnectivitySnackbar(true)
                                    viewModel.startSocketForProduct(product.id)
                                }
                                ConnectionState.Unavailable -> {
                                    showConnectivitySnackbar(false)
                                    viewModel.closeSocketForProduct(product.id)
                                }
                            }
                        }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.detailsUiState
                        .collect {
                            when(it) {
                                is ProductDetailsUiState.ProductUpdate -> {
                                    it.currentPrice?.let { price ->
                                        renderNewPrice(price.toFloat(), price)
                                    }
                                }
                            }
                        }
                }
            }
        }


    }

    private fun renderProduct(product: Product) {
        with(binding) {
            productTitle.text = product.name
            previousClosingPrice.text = product.closingPriceFormatted
            renderNewPrice(product.currentPrice, product.currentPriceFormatted)
        }
    }

    private fun renderNewPrice(price: Float, priceFormatted: String) {
        product?.let {
            with(binding) {
                currentPrice.text = if (currentPrice.text.toString().isNotEmpty()) {
                     priceFormatted + currentPrice.text.toString().last()
                } else {
                    priceFormatted
                }
                val (symbol, diff) = price.percentageDifferenceWith(it.closingPrice)
                with(priceDifference) {
                    text = diff
                    val drawable = if (symbol == "-") {
                        setTextColor(resources.getColor(R.color.red))
                        R.drawable.ic_baseline_arrow_drop_down_24
                    } else {
                        setTextColor(resources.getColor(R.color.blue))
                        R.drawable.ic_baseline_arrow_drop_up_24
                    }
                    setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        product?.let {
            viewModel.closeSocketForProduct(it.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showConnectivitySnackbar(isConnected: Boolean) {
        val message = if (isConnected) {
            getString(R.string.internet_connection_available)
        } else {
            getString(R.string.internet_connection_unavailable)
        }
        Snackbar.make(this.requireView(), message, LENGTH_LONG).show()
    }

    companion object {
        const val PRODUCT_BUNDLE_KEY = "product"
    }
}