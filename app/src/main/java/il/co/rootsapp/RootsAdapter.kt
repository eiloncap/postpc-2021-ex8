package il.co.rootsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RootsAdapter : RecyclerView.Adapter<RootViewHolder>() {

    private val rootsList: MutableList<RootItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_root, parent, false)
        return RootViewHolder(view)
    }

    override fun onBindViewHolder(holder: RootViewHolder, position: Int) {
        val todoItem = rootsList[position]
        val progressBar = holder.progressBar
        val cancelButton = holder.cancelButton
        val deleteButton = holder.deleteButton
        val description = holder.description
    }

    override fun getItemCount(): Int {
        return rootsList.size
    }
}