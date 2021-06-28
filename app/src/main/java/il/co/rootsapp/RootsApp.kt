package il.co.rootsapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.*

class RootsApp : Application(), Configuration.Provider {

    companion object {
        const val SP_ROOT_ITEMS = "sp_roots"
        lateinit var instance: RootsApp
            private set
    }

    lateinit var db: RootsDB
        private set
    lateinit var workManager: WorkManager
    lateinit var sp: SharedPreferences

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()

        // provide custom configuration
        val myConfig = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()

        // initialize WorkManager
        workManager = try {
            WorkManager.getInstance(this)
        } catch (e: Exception) {
            WorkManager.initialize(this, myConfig)
            WorkManager.getInstance(this)
        }
        
        workManager.cancelAllWork()
        instance = this
        sp = getSharedPreferences(SP_ROOT_ITEMS, Context.MODE_PRIVATE)
        db = RootsDB()
    }

    fun startRootsWorker(num: Long, startPoint: Long): UUID {
        Log.d("eilon", "startRootsWorker got params: $num, $startPoint")
        val workRequest = OneTimeWorkRequest.Builder(RootCalculatorWorker::class.java)
            .setInputData(Data.Builder()
                .putLong(RootCalculatorWorker.INPUT_START_TAG, startPoint)
                .putLong(RootCalculatorWorker.INPUT_NUM_TAG, num)
                .build())
            .build()
        workManager.enqueue(workRequest)
        return workRequest.id
    }

    fun cancelRootsWorker(id: UUID) {
        workManager.cancelWorkById(id)
    }
}