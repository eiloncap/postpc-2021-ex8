package il.co.rootsapp

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class RootViewModel : Serializable {

    private val itemsList = TreeSet<RootItem>()

    fun getItems() : ArrayList<RootItem>{
        return ArrayList(itemsList)
    }

    fun addNewItem(item: RootItem) {
        itemsList.add(item)
    }

    fun deleteItem(item: RootItem) {
        itemsList.remove(item)
    }
}