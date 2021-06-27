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
                if (rootItem.root1 == null || rootItem.root2 == null) "${rootItem.num} is prime"
                else "roots for ${rootItem.num} are ${rootItem.root1} and ${rootItem.root2}"
            progressBar.progress = 100
        } else {
            cancelButton.visibility = View.VISIBLE
            deleteButton.visibility = View.GONE
            description.text = "calculating roots for ${rootItem.num}..."
            // todo: get percent from worker
            progressBar.progress = 100 * rootItem.lowerBound / rootItem.num
        }
    }

    override fun getItemCount(): Int {
        return rootsList.size
    }
}