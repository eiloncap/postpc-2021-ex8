package il.co.rootsapp

import android.app.Application

class RootsApp : Application() {

    companion object {
        lateinit var instance: RootsApp
            private set
    }

    var viewModel: RootViewModel? = null
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this
        viewModel = RootViewModel()
    }
}