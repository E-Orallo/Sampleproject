package simple.project.data.cart

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import simple.project.data.local.dao.CartDao
import simple.project.data.local.entity.CartItemEntity
import simple.project.data.model.Product
import simple.project.data.repository.ProductRepository

class CartManager(
    private val cartDao: CartDao,
    productRepository: ProductRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    val cartItems: StateFlow<List<CartItem>> = combine(
        cartDao.getCartItems(),
        productRepository.getProducts()
    ) { entities, products ->
        entities.mapNotNull { entity ->
            val product = products.find { it.id == entity.productId }
            product?.let { CartItem(it, entity.quantity) }
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    fun addToCart(product: Product) {
        scope.launch {
            val existing = cartItems.value.find { it.product.id == product.id }
            val newQuantity = (existing?.quantity ?: 0) + 1
            cartDao.insertOrUpdate(CartItemEntity(product.id, newQuantity))
        }
    }

    fun removeFromCart(productId: Int) {
        scope.launch {
            cartDao.deleteItem(productId)
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        scope.launch {
            if (newQuantity <= 0) {
                cartDao.deleteItem(productId)
            } else {
                cartDao.insertOrUpdate(CartItemEntity(productId, newQuantity))
            }
        }
    }

    fun clearCart() {
        scope.launch {
            cartDao.clearCart()
        }
    }
}

data class CartItem(
    val product: Product,
    val quantity: Int,
)
