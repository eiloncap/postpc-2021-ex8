package il.co.rootsapp

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class RootItem(
    val num: Long,
    var workerId: UUID,
    var lowerBound: Long = 2L,
    var root: Long = 1L,
    var progress: Int = 0,
    private val creationDT: LocalDateTime = LocalDateTime.now()
) : Serializable, Comparable<RootItem> {

    companion object {
        const val DONE = 101
    }

    var isDone: Boolean = false
        get() = this.progress >= DONE
        private set

    override fun compareTo(other: RootItem): Int {
        if (this == other) return 0
        if (isDone == other.isDone) {
            val res = other.creationDT.compareTo(this.creationDT)
            return if (res == 0) -1 else res
        }
        return if (isDone && !other.isDone) 1 else -1
    }

    override fun hashCode(): Int {
        return num.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }
}