package simple.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import simple.project.data.local.dao.ProductDao
import simple.project.data.local.entity.toDomain
import simple.project.data.local.entity.toEntity
import simple.project.data.model.Product
import simple.project.data.model.Rating

class ProductRepository(private val productDao: ProductDao) {
    
    // Curated local catalog for the Philippine Nature & Eco Marketplace
    private val localCatalog = listOf(
        Product(
            id = 1,
            title = "Handwoven Abaca Bayong",
            price = 850.00,
            description = "Sustainable and stylish tote made from durable abaca fibers by local Bicol artisans. Perfect for plastic-free market runs.",
            category = "Local Crafts",
            image = "https://images.unsplash.com/photo-1590739225287-bd2ba5198fad?q=80&w=800",
            rating = Rating(4.8, 124),
        ),
        Product(
            id = 2,
            title = "Organic Virgin Coconut Oil",
            price = 450.00,
            description = "500ml of pure, cold-pressed coconut oil sourced from Quezon province plantations. 100% natural and locally processed.",
            category = "Personal Care",
            image = "https://images.unsplash.com/photo-1620980547016-1632782e463a?q=80&w=800",
            rating = Rating(4.9, 89),
        ),
        Product(
            id = 3,
            title = "Bamboo Utensil Travel Set",
            price = 320.00,
            description = "Reusable spoon, fork, and chopsticks set crafted from sustainable bamboo. Comes in a handwoven inabel pouch.",
            category = "Home & Living",
            image = "https://images.unsplash.com/photo-1594498653385-d5172b532c00?q=80&w=800",
            rating = Rating(4.7, 210),
        ),
        Product(
            id = 4,
            title = "Sampaguita Solid Shampoo",
            price = 280.00,
            description = "Zero-waste shampoo bar with natural sampaguita extracts for healthy hair. Sourced from local refilleries.",
            category = "Personal Care",
            image = "https://images.unsplash.com/photo-1607006344380-b6775a0824a7?q=80&w=800",
            rating = Rating(4.6, 56),
        ),
        Product(
            id = 5,
            title = "Upcycled Inabel Face Mask",
            price = 150.00,
            description = "Handcrafted masks using traditional Ilocano weave scraps. Breathable, washable, and eco-friendly.",
            category = "Clothing",
            image = "https://images.unsplash.com/photo-1598516083072-b8832a76f272?q=80&w=800",
            rating = Rating(4.5, 342),
        ),
        Product(
            id = 6,
            title = "Raw Mountain Wild Honey",
            price = 380.00,
            description = "350g of unprocessed honey harvested from the mountains of Sierra Madre. Supporting local forest gatherers.",
            category = "Food & Beverage",
            image = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?q=80&w=800",
            rating = Rating(4.9, 156),
        ),
        Product(
            id = 7,
            title = "Handcrafted Bamboo Tumbler",
            price = 550.00,
            description = "Double-walled insulation with a natural bamboo exterior. Keep your drinks cold while reducing plastic waste.",
            category = "Home & Living",
            image = "https://images.unsplash.com/photo-1622467824213-9097e3355523?q=80&w=800",
            rating = Rating(4.7, 92),
        ),
        Product(
            id = 8,
            title = "Natural Loom-Woven Blanket",
            price = 1200.00,
            description = "Large, cozy blanket handwoven using traditional methods in the Cordillera region. 100% cotton fibers.",
            category = "Local Crafts",
            image = "https://images.unsplash.com/photo-1618220179428-22790b461013?q=80&w=800",
            rating = Rating(4.8, 45),
        )
    )

    suspend fun initializeDatabase() {
        if (productDao.getCount() == 0) {
            productDao.insertProducts(localCatalog.map { it.toEntity() })
        }
    }

    fun getProducts(): Flow<List<Product>> = productDao.getProducts().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getProduct(id: Int): Product? = productDao.getProductById(id)?.toDomain()
}
