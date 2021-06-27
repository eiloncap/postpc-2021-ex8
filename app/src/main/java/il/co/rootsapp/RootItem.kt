package il.co.rootsapp

import java.io.Serializable
import java.time.LocalDateTime

data class RootItem(
    val num: Long,
    val lowerBound: Long = 0L,
    var root1: Long? = null,
    var root2: Long? = null,
    var isDone: Boolean = false,
    private val creationDT: LocalDateTime = LocalDateTime.now()
) : Serializable, Comparable<RootItem> {

    override fun compareTo(other: RootItem): Int {
        if (this == other) {
            return 0
        }
        if (isDone == other.isDone) {
            val res = other.creationDT.compareTo(this.creationDT)
            return if (res == 0) -1 else res
        }
        return if (isDone && !other.isDone) 1 else -1
    }
}