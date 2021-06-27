package il.co.rootsapp

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.math.sqrt

class RootCalculatorWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val NO_ROOT: Long = 0L
    }

    override fun doWork(): Result {
        val numberToCalculateRootsFor: Long = inputData.getLong("user_id", -1L)
        if (numberToCalculateRootsFor == -1L) return Result.failure()
        for (i in 2L..sqrt(numberToCalculateRootsFor.toDouble()).toLong()) {
            if (numberToCalculateRootsFor % i == 0L) {
                return Result.success(Data.Builder().putLong("root1", i).build())
            }
        }
        return Result.success(Data.Builder().putLong("root1", NO_ROOT).build())
    }
}