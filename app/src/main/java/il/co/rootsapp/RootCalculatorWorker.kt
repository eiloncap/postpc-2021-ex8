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
        const val INPUT_TAG = "number_to_root"
        const val OUTPUT_TAG = "root1"
    }

    override fun doWork(): Result {
        val numberToRoot: Long = inputData.getLong(INPUT_TAG, -1L)
        if (numberToRoot <= 0L) {
            return Result.failure()
        }
        for (i in 2L..sqrt(numberToRoot.toDouble()).toLong()) {
            if (numberToRoot % i == 0L) {
                return Result.success(Data.Builder().putLong(OUTPUT_TAG, i).build())
            }
        }
        return Result.success(Data.Builder().putLong(OUTPUT_TAG, NO_ROOT).build())
    }
}