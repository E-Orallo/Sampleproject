package simple.project.data.wishlist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object WishlistManager {
    private val _wishlistIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistIds: StateFlow<Set<Int>> = _wishlistIds.asStateFlow()

    fun toggleWishlist(productId: Int) {
        _wishlistIds.update { current ->
            if (current.contains(productId)) current - productId else current + productId
        }
    }

    fun isWishlisted(productId: Int): Boolean = _wishlistIds.value.contains(productId)
}
