package il.co.rootsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RootsDB : Serializable {

    private val itemsList = HashMap<RootItem, UUID>()
    val items: ArrayList<RootItem>
        get() = ArrayList(itemsList.keys.toSortedSet())
    private val mld: MutableLiveData<List<RootItem>> = MutableLiveData()
    val listLiveData: LiveData<List<RootItem>> = mld


    init {
        mld.value = items
    }

    fun addNewItem(num: Long) {
        val item = RootItem(num = num)
        val id = RootsApp.instance.startRootsWorker(item.num, item.lowerBound)
        itemsList[item] = id
        RootsApp.instance.workManager.getWorkInfoByIdLiveData(id).observeForever { workInfo ->
            if (workInfo == null) return@observeForever
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    item.progress = RootItem.DONE
                    item.root = workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, num)
                    mld.value = items
                }
                WorkInfo.State.RUNNING -> {
                    item.progress = workInfo.progress.getInt(RootCalculatorWorker.PROGRESS, 0)
                    item.lowerBound = workInfo.progress.getLong(RootCalculatorWorker.LOWE_BOUND, 2L)
                    mld.value = items
                }
                WorkInfo.State.FAILED -> { // handle failure
                }
                else -> { // do nothing. state could be SUCCEEDED, FAILED, ENQUEUED, RUNNING, BLOCKED, or CANCELED
                }
            }
        }

    }

    fun cancelItem(item: RootItem) {
        RootsApp.instance.cancelRootsWorker(itemsList[item]!!)
        deleteItem(item)
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
        mld.value = items
    }
}