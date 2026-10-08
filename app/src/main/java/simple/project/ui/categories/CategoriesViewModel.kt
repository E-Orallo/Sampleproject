package simple.project.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import simple.project.data.repository.ProductRepository

data class CategoryItem(
    val name: String,
    val icon: String, // We'll use this for Unsplash images per category
    val count: Int
)

class CategoriesViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    val categories: StateFlow<List<CategoryItem>> = repository.getProducts().map { products ->
        products.groupBy { it.category }.map { (name, list) ->
            CategoryItem(
                name = name,
                icon = getCategoryIcon(name),
                count = list.size
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun getCategoryIcon(category: String): String {
        return when (category) {
            "Local Crafts" -> "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?q=80&w=400"
            "Personal Care" -> "https://images.unsplash.com/photo-1540555700478-4be289fbece8?q=80&w=400"
            "Home & Living" -> "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?q=80&w=400"
            "Clothing" -> "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?q=80&w=400"
            "Food & Beverage" -> "https://images.unsplash.com/photo-1542838132-92c53300491e?q=80&w=400"
            else -> "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?q=80&w=400"
        }
    }
}
