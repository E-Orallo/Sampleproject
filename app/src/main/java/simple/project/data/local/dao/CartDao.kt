package simple.project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import simple.project.data.local.entity.CartItemEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity): Unit

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteItem(productId: Int): Unit

    @Query("DELETE FROM cart_items")
    suspend fun clearCart(): Unit
}
