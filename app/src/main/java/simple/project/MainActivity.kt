package simple.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import simple.project.ui.details.ProductDetailsScreen
import simple.project.ui.details.ProductDetailsViewModel
import simple.project.ui.discovery.ProductDiscoveryScreen
import simple.project.ui.discovery.ProductDiscoveryViewModel
import simple.project.ui.navigation.ProductDetails
import simple.project.ui.navigation.ProductDiscovery
import simple.project.ui.navigation.Cart
import simple.project.ui.navigation.Checkout
import simple.project.ui.cart.CartScreen
import simple.project.ui.cart.CartViewModel
import simple.project.ui.checkout.CheckoutScreen
import simple.project.ui.checkout.CheckoutViewModel
import simple.project.ui.categories.CategoriesScreen
import simple.project.ui.categories.CategoriesViewModel
import simple.project.ui.about.AboutScreen
import simple.project.ui.login.LoginScreen
import simple.project.ui.login.LoginViewModel
import simple.project.ui.navigation.About
import simple.project.ui.navigation.Categories
import simple.project.ui.navigation.Login
import simple.project.ui.navigation.Profile
import simple.project.ui.theme.SampleprojectTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
        )
        setContent {
            SampleprojectTheme {
                ProductApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProductApp() {
    val context = LocalContext.current
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(Login)
    val currentRoute = backStack.lastOrNull() ?: Login

    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    val entryProvider = entryProvider {
        entry<Login> {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    backStack.clear()
                    backStack.add(ProductDiscovery())
                }
            )
        }
        entry<ProductDiscovery> { route ->
            val viewModel: ProductDiscoveryViewModel = viewModel(
                key = "discovery_${route.category}",
                factory = remember(route.category) {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            val repo = Dependencies.getProductRepository(context)
                            return ProductDiscoveryViewModel(repo, route.category) as T
                        }
                    }
                }
            )
            ProductDiscoveryScreen(
                viewModel = viewModel,
                cartManager = Dependencies.getCartManager(context),
                onProductClick = { product ->
                    backStack.add(ProductDetails(product.id))
                },
                onCartClick = {
                    backStack.add(Cart)
                },
                onAboutClick = {
                    backStack.add(About)
                },
            )
        }
        entry<Categories> {
            val viewModel: CategoriesViewModel = viewModel(
                factory = remember {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return CategoriesViewModel(Dependencies.getProductRepository(context)) as T
                        }
                    }
                }
            )
            CategoriesScreen(
                viewModel = viewModel,
                onCategoryClick = { category ->
                    backStack.clear()
                    backStack.add(ProductDiscovery(category))
                }
            )
        }
        entry<Profile> {
            ProfileScreen(
                onLogout = {
                    backStack.clear()
                    backStack.add(Login)
                },
                onAboutClick = {
                    backStack.add(About)
                }
            )
        }
        entry<ProductDetails>(
            metadata = ListDetailSceneStrategy.detailPane(),
        ) { route ->
            val viewModel: ProductDetailsViewModel = viewModel(
                key = "details_${route.productId}",
                factory = remember(route.productId) {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return ProductDetailsViewModel(
                                route.productId,
                                Dependencies.getProductRepository(context),
                                Dependencies.getCartManager(context),
                            ) as T
                        }
                    }
                },
            )
            ProductDetailsScreen(
                viewModel = viewModel,
                onBack = { backStack.removeLastOrNull() },
                onCartClick = {
                    backStack.add(Cart)
                },
            )
        }
        entry<Cart> {
            val viewModel: CartViewModel = viewModel(
                factory = remember {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return CartViewModel(Dependencies.getCartManager(context)) as T
                        }
                    }
                }
            )
            CartScreen(
                viewModel = viewModel,
                onBack = { backStack.removeLastOrNull() },
            ) { backStack.add(Checkout) }
        }
        entry<Checkout> {
            val viewModel: CheckoutViewModel = viewModel(
                factory = remember {
                    object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return CheckoutViewModel(Dependencies.getCartManager(context)) as T
                        }
                    }
                }
            )
            CheckoutScreen(
                viewModel = viewModel,
                onBack = { backStack.removeLastOrNull() },
            ) {
                backStack.remove(Checkout)
                backStack.remove(Cart)
            }
        }
        entry<About> {
            AboutScreen { backStack.removeLastOrNull() }
        }
    }

    val entries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider,
    )

    Scaffold(
        bottomBar = {
            if ((currentRoute is ProductDiscovery) || (currentRoute == Categories) || (currentRoute == Profile) || (currentRoute == Cart)) {
                EcoBottomNavigation(
                    currentRoute = currentRoute,
                ) { route ->
                    if (currentRoute != route) {
                        // Clear backstack and add new tab
                        backStack.clear()
                        backStack.add(route)
                    }
                }
            }
        }
    ) { padding ->
        NavDisplay(
            entries = entries,
            sceneStrategies = listOf(listDetailStrategy),
            modifier = Modifier.padding(padding)
        ) {
            backStack.removeLastOrNull()
        }
    }
}

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onAboutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hello, Admin!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAboutClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("About Eco Marketplace", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Log Out", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EcoBottomNavigation(
    currentRoute: NavKey,
    onTabSelected: (NavKey) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute is ProductDiscovery,
            onClick = { onTabSelected(ProductDiscovery()) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == Categories,
            onClick = { onTabSelected(Categories) },
            icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
            label = { Text("Categories") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == Cart,
            onClick = { onTabSelected(Cart) },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == Profile,
            onClick = { onTabSelected(Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
    }
}
