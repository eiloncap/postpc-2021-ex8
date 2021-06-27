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
        const val INPUT_START_TAG = "start_from"
        const val INPUT_NUM_TAG = "number_to_root"
        const val PROGRESS = "progress"
        const val LOWE_BOUND = "lower_bound"
        const val OUTPUT_TAG = "root"
    }

    override suspend fun doWork(): Result {
        val timeStartMs = System.currentTimeMillis()
        val numberToRoot: Long = inputData.getLong(INPUT_NUM_TAG, -1L)
        val startFrom: Long = inputData.getLong(INPUT_START_TAG, 2L).coerceAtLeast(2L)
        setProgress(workDataOf(PROGRESS to 0))
        if (numberToRoot < 0L) return Result.failure()

        val upperBound = sqrt(numberToRoot.toDouble()).toLong()
        var prog = 0
        for (i in startFrom..upperBound) {
            val next = (100 * i / upperBound).toInt()
            if (next > prog || System.currentTimeMillis() - timeStartMs >= 600_000L) {
                setProgress(workDataOf(PROGRESS to (100 * i / upperBound).toInt()))
                setProgress(workDataOf(PROGRESS to i))
                prog = next
            }
            if (numberToRoot % i == 0L) {
                return Result.success(Data.Builder().putLong(OUTPUT_TAG, i).build())
            }
        }
        return Result.success(Data.Builder().putLong(OUTPUT_TAG, numberToRoot).build())
    }
}