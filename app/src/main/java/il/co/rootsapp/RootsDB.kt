package il.co.rootsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
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
        val item = RootItem(num = num)
        itemsList.add(item)
        RootsApp.instance.startRootsWorker(item.num).observeForever { workInfo ->
            if (workInfo == null) return@observeForever
            else if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                val output = workInfo.outputData
                item.isDone = true
                val root1 = output.getLong(RootCalculatorWorker.OUTPUT_TAG, RootCalculatorWorker.NO_ROOT)
                if (root1 != RootCalculatorWorker.NO_ROOT) {
                    item.root1 = root1
                    item.root2 = item.num / root1
                }
            } else if (workInfo.state == WorkInfo.State.FAILED) {
                val output = workInfo.outputData
                // handle failure
            } else {
                // do nothing. state could be SUCCEEDED, FAILED, ENQUEUED, RUNNING, BLOCKED, or CANCELED

            }
        }

    }

    fun cancelItem(item: RootItem) {
        itemsList.remove(item)
        // todo: stop worker
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
    }
}