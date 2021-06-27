package il.co.rootsapp

import android.app.Application
import androidx.work.WorkManager

class RootsApp : Application() {

    companion object {
        lateinit var instance: RootsApp
            private set
    }

    var viewModel: RootViewModel? = null
        private set

    override fun onCreate() {
        super.onCreate()
        val workManager = WorkManager.getInstance(this)

        instance = this
        viewModel = RootViewModel()
    }
}