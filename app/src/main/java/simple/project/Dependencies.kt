package simple.project

import android.content.Context
import simple.project.data.cart.CartManager
import simple.project.data.repository.ProductRepository

object Dependencies {
    private var _productRepository: ProductRepository? = null
    private var _cartManager: CartManager? = null

    fun getProductRepository(context: Context): ProductRepository {
        return _productRepository ?: synchronized(this) {
            val db = (context.applicationContext as MainApplication).database
            val repo = ProductRepository(db.productDao())
            _productRepository = repo
            repo
        }
    }

    fun getCartManager(context: Context): CartManager {
        return _cartManager ?: synchronized(this) {
            val db = (context.applicationContext as MainApplication).database
            val repo = getProductRepository(context)
            val manager = CartManager(db.cartDao(), repo)
            _cartManager = manager
            manager
        }
    }
}
