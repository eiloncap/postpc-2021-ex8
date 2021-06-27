package il.co.rootsapp

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager

class RootsApp : Application() {

    companion object {
        lateinit var instance: RootsApp
            private set
    }

    var viewModel: RootsDB? = null
        private set

    override fun onCreate() {
        super.onCreate()
        val workManager = WorkManager.getInstance(this)

        instance = this
        viewModel = RootsDB()
    }

    fun startRootsWorker(num: Long): LiveData<WorkInfo> {
        val workRequest = OneTimeWorkRequest.Builder(RootCalculatorWorker::class.java)
            .setInputData(Data.Builder().putLong(RootCalculatorWorker.INPUT_TAG, num).build())
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(workRequest)

        return workManager.getWorkInfoByIdLiveData(workRequest.id)
    }
}