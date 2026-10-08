package simple.project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import simple.project.data.model.Product
import simple.project.data.model.Rating

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rate: Double,
    val count: Int
)

fun ProductEntity.toDomain() = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = Rating(rate, count)
)

fun Product.toEntity() = ProductEntity(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rate = rating.rate,
    count = rating.count
)
