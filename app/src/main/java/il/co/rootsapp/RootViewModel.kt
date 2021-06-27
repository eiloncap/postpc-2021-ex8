package il.co.rootsapp

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class RootViewModel : Serializable {

    private val itemsList = TreeSet<RootItem>()
    val items: ArrayList<RootItem>
        get() = ArrayList(itemsList)

    fun addNewItem(item: RootItem) {
        itemsList.add(item)
        // todo: call worker
        RootsApp.instance.startRootsWorker(item.num)

    }

    fun cancelItem(item: RootItem) {
        itemsList.remove(item)
        // todo: stop worker
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
    }
}