package simple.project.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import simple.project.data.cart.CartManager
import simple.project.data.model.Product
import simple.project.data.repository.ProductRepository

class ProductDetailsViewModel(
    private val productId: Int,
    private val repository: ProductRepository,
    private val cartManager: CartManager,
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _product.value = repository.getProduct(productId)
            } catch (_: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart() {
        _product.value?.let {
            cartManager.addToCart(it)
        }
    }
}
