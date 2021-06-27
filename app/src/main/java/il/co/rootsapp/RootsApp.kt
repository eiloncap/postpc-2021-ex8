package il.co.rootsapp

import android.app.Application
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.*

class RootsApp : Application() {

    companion object {
        lateinit var instance: RootsApp
            private set
    }

    lateinit var db: RootsDB
        private set
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        workManager = WorkManager.getInstance(this)

        instance = this
        db = RootsDB()
    }

    fun startRootsWorker(num: Long): UUID {
        val workRequest = OneTimeWorkRequest.Builder(RootCalculatorWorker::class.java)
            .setInputData(Data.Builder().putLong(RootCalculatorWorker.INPUT_TAG, num).build())
            .build()
        workManager.enqueue(workRequest)
        return workRequest.id
    }

    fun cancelRootsWorker(id: UUID) {
        workManager.cancelWorkById(id)
    }
}