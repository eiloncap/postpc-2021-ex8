package il.co.rootsapp

import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RootViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    val description: TextView = view.findViewById(R.id.descriptionText)
    val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    val cancelButton: ImageButton = view.findViewById(R.id.cancelButton)
}