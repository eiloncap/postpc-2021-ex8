package il.co.rootsapp

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class RootItem(
    val num: Int,
    val root1: Int? = null,
    val root2: Int? = null,
    var isDone: Boolean = false,
    private val creationDT: LocalDateTime = LocalDateTime.now()
) : Serializable, Comparable<RootItem> {

    override fun compareTo(other: RootItem): Int {
        if (this == other) {
            return 0
        }
        val res = other.creationDT.compareTo(this.creationDT)
        return if (res == 0) -1 else res
    }
}