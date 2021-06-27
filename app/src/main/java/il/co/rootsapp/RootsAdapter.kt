package il.co.rootsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RootsAdapter : RecyclerView.Adapter<RootViewHolder>() {

    private val rootsList: MutableList<RootItem> = ArrayList()

    fun setItems(newRootsList: List<RootItem>) {
        rootsList.clear()
        rootsList.addAll(newRootsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_root, parent, false)
        return RootViewHolder(view)
    }

    override fun onBindViewHolder(holder: RootViewHolder, position: Int) {
        // todo: general for all kind of items
        val RootItem = rootsList[position]
        val progressBar = holder.progressBar
        val cancelButton = holder.cancelButton
        val deleteButton = holder.deleteButton
        val description = holder.description
        description.text = RootItem.num.toString()
        progressBar.progress = 0
    }

    override fun getItemCount(): Int {
        return rootsList.size
    }
}