package il.co.rootsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class RootsDB : Serializable {

    private val itemsList = TreeSet<RootItem>()
    val items: ArrayList<RootItem>
        get() = ArrayList(itemsList)
    private val mld: MutableLiveData<List<RootItem>> = MutableLiveData()
    val listLiveData: LiveData<List<RootItem>> = mld


    init {
        mld.value = items
    }

    fun addNewItem(num: Long) {
        val id = RootsApp.instance.startRootsWorker(num)
        val item = RootItem(workId = id, num = num)
        itemsList.add(item)
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
        RootsApp.instance.cancelRootsWorker(item.workId)
        deleteItem(item)
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
        mld.value = items
    }
}