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

    //todo: 1) order problems - solved?
    //      2) not running problems - solved?
    //      3) slow startup
    //      4) duplicates - solved?
    //      5) delete not responding - solved?

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
            Log.d("eilon", "reading  ${ser.value as String}")
            val item = gson.fromJson(ser.value as String, RootItem::class.java)
            itemsList.add(item)
            // todo: check if work id is active
            if (!RootsApp.instance.workManager.getWorkInfoById(item.workerId).isDone) {
                RootsApp.instance.workManager.cancelWorkById(item.workerId)
            }
            if (!item.isDone) {
                removeItemFromSp(item)
                item.workerId = RootsApp.instance.startRootsWorker(item.num, item.lowerBound)
                observeRootResults(item)
                addUpdateItemToSp(item)

            }
        }
        mld.value = items
    }

    private fun observeRootResults(item: RootItem) {
        // todo: if workerId exists, use it
        RootsApp.instance.workManager.getWorkInfoByIdLiveData(item.workerId)
            .observeForever { workInfo ->
                if (workInfo == null) return@observeForever
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        itemsList.remove(item)
                        item.progress = RootItem.DONE
                        item.root =
                            workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, item.num)
                        itemsList.add(item)
                        addUpdateItemToSp(item)
                        mld.value = items
                    }
                    WorkInfo.State.RUNNING -> {
                        workInfo.progress.getInt(RootCalculatorWorker.PROGRESS, item.progress)
                            .let { if (it > item.progress) item.progress = it}
                        workInfo.progress.getLong(RootCalculatorWorker.LOWER_BOUND, item.lowerBound)
                            .let { if (it > item.lowerBound) item.lowerBound = it}

                        addUpdateItemToSp(item)
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
        itemsList.add(item)
        addUpdateItemToSp(item)
        observeRootResults(item)
        mld.value = items
    }

    fun cancelItem(item: RootItem) {
        RootsApp.instance.cancelRootsWorker(item.workerId)
        deleteItem(item)
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
        mld.value = items
        removeItemFromSp(item)
    }

    private fun addUpdateItemToSp(item: RootItem) {
        RootsApp.instance.sp.edit().putString(item.workerId.toString(), gson.toJson(item)).apply()
    }

    private fun removeItemFromSp(item: RootItem) {
        RootsApp.instance.sp.edit().remove(item.workerId.toString()).apply()
    }
}