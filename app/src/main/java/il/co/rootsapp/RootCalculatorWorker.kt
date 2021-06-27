package il.co.rootsapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlin.math.sqrt

class RootCalculatorWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    companion object {
        const val INPUT_TAG = "number_to_root"
        const val PROGRESS = "Progress"
        const val OUTPUT_TAG = "root"
    }

    override suspend fun doWork(): Result {
        val numberToRoot: Long = inputData.getLong(INPUT_TAG, -1L)
        setProgress(workDataOf(PROGRESS to 0))
        if (numberToRoot < 0L) {
            return Result.failure()
        }
        val upperBound = sqrt(numberToRoot.toDouble()).toLong()
        for (i in 2L..upperBound) {
            setProgress(workDataOf(PROGRESS to (100 * i / upperBound).toInt()))
            if (numberToRoot % i == 0L) {
                return Result.success(Data.Builder().putLong(OUTPUT_TAG, i).build())
            }
        }
        return Result.success(Data.Builder().putLong(OUTPUT_TAG, numberToRoot).build())
    }
}