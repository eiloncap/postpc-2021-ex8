package il.co.rootsapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import com.google.gson.GsonBuilder
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class RootsDB : Serializable {

    private val itemsList = TreeSet<RootItem>()
    private val gson = GsonBuilder().registerTypeAdapter(
        LocalDateTime::class.java, LocalDateTimeAdapter()
    )
        .create()
    val items: ArrayList<RootItem>
        get() = ArrayList(itemsList)
    private val mld: MutableLiveData<List<RootItem>> = MutableLiveData()
    val listLiveData: LiveData<List<RootItem>> = mld


    init {
        RootsApp.instance.sp.all.forEach { ser ->
            Log.d("eilon", "jsoneditemB = ${ser.value as String}")
            val item = gson.fromJson(ser.value as String, RootItem::class.java)
            itemsList.add(item)
            // todo: check serialization of date
            if (!item.isDone) {
                calculateRoot(item)
            }
        }
        mld.value = items
    }

    private fun calculateRoot(item: RootItem) {
        // todo: if workerId exists, use it
        RootsApp.instance.workManager.getWorkInfoByIdLiveData(item.workerId)
            .observeForever { workInfo ->
                if (workInfo == null) return@observeForever
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        item.progress = RootItem.DONE
                        item.root =
                            workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, item.num)
                        mld.value = items
                    }
                    WorkInfo.State.RUNNING -> {
                        item.progress =
                            workInfo.progress.getInt(RootCalculatorWorker.PROGRESS, item.progress)
                        item.lowerBound =
                            workInfo.progress.getLong(
                                RootCalculatorWorker.LOWE_BOUND,
                                item.lowerBound
                            )
                        mld.value = items
                    }
                    WorkInfo.State.FAILED -> { // handle failure
                    }
                    else -> { // do nothing. state could be SUCCEEDED, FAILED, ENQUEUED, RUNNING, BLOCKED, or CANCELED
                    }
                }
            }
    }

    fun addNewItem(num: Long) {
        val id = RootsApp.instance.startRootsWorker(num, 2L)
        val item = RootItem(num = num, workerId = id)
        calculateRoot(item)
        itemsList.add(item)
        Log.d("eilon", "jsoneditemA = ${gson.toJson(item)}")
        RootsApp.instance.sp.edit().putString(item.workerId.toString(), gson.toJson(item)).apply()
        mld.value = items
    }

    fun cancelItem(item: RootItem) {
        RootsApp.instance.cancelRootsWorker(item.workerId)
        deleteItem(item)
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
        mld.value = items
        RootsApp.instance.sp.edit().remove(item.workerId.toString()).apply()
    }
}