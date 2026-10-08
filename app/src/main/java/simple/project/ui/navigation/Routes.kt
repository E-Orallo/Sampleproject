package simple.project.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object Login : NavKey

@Serializable
data class ProductDiscovery(val category: String? = null) : NavKey

@Serializable
object Categories : NavKey

@Serializable
data class ProductDetails(val productId: Int) : NavKey

@Serializable
object Cart : NavKey

@Serializable
object Checkout : NavKey

@Serializable
object About : NavKey

@Serializable
object Profile : NavKey
