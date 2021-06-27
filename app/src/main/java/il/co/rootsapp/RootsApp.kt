package il.co.rootsapp

import android.app.Application
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
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

    fun startRootsWorker(num: Long) {
        val workRequest = OneTimeWorkRequest.Builder(RootCalculatorWorker::class.java)
            .setInputData(Data.Builder().putLong(RootCalculatorWorker.INPUT_TAG, num).build())
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}