package il.co.rootsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // todo: implement buttons
        val rootItem = rootsList[position]
        val progressBar = holder.progressBar
        val cancelButton = holder.cancelButton
        val deleteButton = holder.deleteButton
        val description = holder.description

        if (rootItem.isDone) {
            cancelButton.visibility = View.GONE
            deleteButton.visibility = View.VISIBLE
            description.text =
                if (rootItem.root == rootItem.num) "${rootItem.num} is prime"
                else "roots for ${rootItem.num} are ${rootItem.root} and ${rootItem.num / rootItem.root}"
        } else {
            cancelButton.visibility = View.VISIBLE
            deleteButton.visibility = View.GONE
            description.text = "calculating roots for ${rootItem.num}..."
        }
        progressBar.progress = rootItem.progress
    }

    override fun getItemCount(): Int {
        return rootsList.size
    }
}