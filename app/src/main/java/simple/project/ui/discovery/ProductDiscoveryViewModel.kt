package simple.project.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import simple.project.data.model.Product
import simple.project.data.repository.ProductRepository

enum class SortOrder {
    Newest,
    PriceLowHigh,
    PriceHighLow,
    Popularity
}

class ProductDiscoveryViewModel(
    private val repository: ProductRepository,
    initialCategory: String? = null
) : ViewModel() {

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(initialCategory)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.Newest)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        repository.getProducts(),
        _searchQuery,
        _selectedCategory,
        _sortOrder,
    ) { products, query, category, sort ->
        val filtered = products.filter { product ->
            val matchesQuery = query.isEmpty() || product.title.contains(query, ignoreCase = true)
            val matchesCategory = (category == null) || (product.category == category)
            matchesQuery && matchesCategory
        }
        
        when (sort) {
            SortOrder.Newest -> filtered.reversed()
            SortOrder.PriceLowHigh -> filtered.sortedBy { it.price }
            SortOrder.PriceHighLow -> filtered.sortedByDescending { it.price }
            SortOrder.Popularity -> filtered.sortedByDescending { it.rating.rate }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<String>> = repository.getProducts().combine(MutableStateFlow(Unit)) { products, _ ->
        products.asSequence().map { it.category }.distinct().toList()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            _isLoading.value = true
            repository.initializeDatabase()
            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String?) {
        _selectedCategory.value = category
    }

    fun onSortOrderChange(order: SortOrder) {
        _sortOrder.value = order
    }
}
