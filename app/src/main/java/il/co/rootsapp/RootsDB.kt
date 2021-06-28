package il.co.rootsapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import com.google.gson.GsonBuilder
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*


class RootsDB : ViewModel(), Serializable {
    private val gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
    private val mld: MutableLiveData<TreeSet<RootItem>> = MutableLiveData(TreeSet<RootItem>())
    val listLiveData: LiveData<TreeSet<RootItem>> = mld


    init {
        RootsApp.instance.sp.all.forEach { ser ->
            val item = gson.fromJson(ser.value as String, RootItem::class.java)
            if (!item.isDone) {
                removeItemFromSp(item)
                item.workerId = RootsApp.instance.startRootsWorker(item.num, item.lowerBound)
                addUpdateItemToSp(item)
                observeRootResults(item)
            }
            mld.value?.add(item)
        }
        mld.value = mld.value
    }

    private fun observeRootResults(item: RootItem) {
        RootsApp.instance.workManager.getWorkInfoByIdLiveData(item.workerId)
            .observeForever { workInfo ->
                if (workInfo == null) return@observeForever
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        mld.value?.remove(item)
                        item.progress = RootItem.DONE
                        item.root =
                            workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, item.num)
                        mld.value?.add(item)
                        addUpdateItemToSp(item)
                        mld.value = mld.value
                    }
                    WorkInfo.State.RUNNING -> {
                        item.progress =
                            workInfo.progress.getInt(RootCalculatorWorker.PROGRESS, item.progress)
                        item.lowerBound = workInfo.progress.getLong(
                            RootCalculatorWorker.LOWER_BOUND,
                            item.lowerBound
                        )
                        addUpdateItemToSp(item)
                        mld.value = mld.value
                    }
                    WorkInfo.State.FAILED -> {
                        item.lowerBound =
                            workInfo.outputData.getLong(RootCalculatorWorker.OUTPUT_TAG, item.num)
                        removeItemFromSp(item)
                        RootsApp.instance.cancelRootsWorker(item.workerId)
                        item.workerId =
                            RootsApp.instance.startRootsWorker(item.num, item.lowerBound)
                        addUpdateItemToSp(item)
                        observeRootResults(item)
                    }
                    else -> { // do nothing. state could be ENQUEUED, BLOCKED, or CANCELED
                    }
                }
            }
    }

    fun addNewItem(num: Long) {
        val id = RootsApp.instance.startRootsWorker(num, 2L)
        val item = RootItem(num = num, workerId = id)
        mld.value?.add(item)
        mld.value = mld.value
        addUpdateItemToSp(item)
        observeRootResults(item)
    }

    fun cancelItem(item: RootItem) {
        deleteItem(item)
        RootsApp.instance.cancelRootsWorker(item.workerId)
    }

    fun deleteItem(item: RootItem) {
        mld.value?.remove(item)
        mld.value = mld.value
        removeItemFromSp(item)
    }

    private fun addUpdateItemToSp(item: RootItem) {
        RootsApp.instance.sp.edit().putString(item.workerId.toString(), gson.toJson(item)).apply()
    }

    private fun removeItemFromSp(item: RootItem) {
        RootsApp.instance.sp.edit().remove(item.workerId.toString()).apply()
    }
}