package il.co.rootsapp

import android.content.Context
import android.util.Log
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
        const val LOWER_BOUND = "lower_bound"
        const val OUTPUT_TAG = "root"
    }

    override suspend fun doWork(): Result {
        val timeStartMs = System.currentTimeMillis()
        val numberToRoot: Long = inputData.getLong(INPUT_NUM_TAG, -1L)
        val startFrom: Long = inputData.getLong(INPUT_START_TAG, 2L)
        Log.d("eilon", "started calculating $numberToRoot from $startFrom, workerID = $id")
        if (numberToRoot < 0L) return Result.failure()
        val upperBound : Long = sqrt(numberToRoot.toDouble()).toLong()
        setProgress(workDataOf(PROGRESS to (100 * (startFrom - 2L) / (upperBound - 2L)).toInt()))
        var prog = 0
        for (i in startFrom..upperBound) {
            val next = (100 * (i - 2L) / (upperBound - 2L)).toInt()
            if (next > prog) {
                setProgress(workDataOf(PROGRESS to next, LOWER_BOUND to i))
                Log.d("eilon", "$numberToRoot sent progress = $next instead of $prog")
                prog = next
            }
            if (System.currentTimeMillis() - timeStartMs >= 600_000L) {
//            if (System.currentTimeMillis() - timeStartMs >= 5_000L) {
                return Result.failure(Data.Builder().putLong(OUTPUT_TAG, i).build())
            }
            if (numberToRoot % i == 0L) {
                return Result.success(Data.Builder().putLong(OUTPUT_TAG, i).build())
            }
        }
        return Result.success(Data.Builder().putLong(OUTPUT_TAG, numberToRoot).build())
    }
}