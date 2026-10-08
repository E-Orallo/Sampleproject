package simple.project.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import simple.project.data.local.dao.CartDao
import simple.project.data.local.dao.ProductDao
import simple.project.data.local.entity.CartItemEntity
import simple.project.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, CartItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eco_database",
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
