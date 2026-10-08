package simple.project

import android.app.Application
import simple.project.data.local.AppDatabase

class MainApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
