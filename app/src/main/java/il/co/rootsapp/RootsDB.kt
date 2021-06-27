package il.co.rootsapp

import android.util.Log
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
        val item = RootItem(num = num)
        itemsList.add(item)
        RootsApp.instance.startRootsWorker(item.num).observeForever { workInfo ->
            if (workInfo == null) return@observeForever
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    item.progress = RootItem.DONE
                    Log.d("eilon", "progress = ${item.progress}")
                    item.root = workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, num)
                    mld.value = items
                }
                WorkInfo.State.RUNNING -> {
                    item.progress = workInfo.progress.getInt(RootCalculatorWorker.PROGRESS, 0)
                    mld.value = items
                    Log.d("eilon", "running progress = ${item.progress}")
                }
                WorkInfo.State.FAILED -> { // handle failure
                }
                else -> { // do nothing. state could be SUCCEEDED, FAILED, ENQUEUED, RUNNING, BLOCKED, or CANCELED
                }
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