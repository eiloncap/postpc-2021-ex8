package il.co.rootsapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.*

class RootsApp : Application() {

    companion object {
        const val SP_ROOT_ITEMS = "sp_roots"
        lateinit var instance: RootsApp
            private set
    }

    lateinit var db: RootsDB
        private set
    lateinit var workManager: WorkManager
    lateinit var sp: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        workManager = WorkManager.getInstance(this)
        sp = getSharedPreferences(SP_ROOT_ITEMS, Context.MODE_PRIVATE)
        db = RootsDB()
    }

    fun startRootsWorker(num: Long, startPoint: Long): UUID {
        val workRequest = OneTimeWorkRequest.Builder(RootCalculatorWorker::class.java)
            .setInputData(Data.Builder().putLong(RootCalculatorWorker.INPUT_START_TAG, startPoint).build())
            .setInputData(Data.Builder().putLong(RootCalculatorWorker.INPUT_NUM_TAG, num).build())
            .build()
        workManager.enqueue(workRequest)
        return workRequest.id
    }

    fun cancelRootsWorker(id: UUID) {
        workManager.cancelWorkById(id)
    }
}