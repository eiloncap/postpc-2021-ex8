package il.co.rootsapp;

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    var viewModel: RootViewModel? = null
    private lateinit var adapter: RootsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = if (viewModel == null) viewModel else RootsApp.instance.viewModel

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        val rootsRecycleView = findViewById<RecyclerView>(R.id.rootsRecycleView)

        val alert = initDialog()

        rootsRecycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        addButton.setOnClickListener {
            alert.show()
        }
    }

    private fun initAdapter() {
        adapter = RootsAdapter()
    }

    private fun initDialog(): Dialog {
        return AlertDialog.Builder(this)
            .setMessage("Enter a number to calculate:")
            .setCancelable(true)
            .setView(layoutInflater.inflate(R.layout.input_alert, null))
            .setPositiveButton("OK") { dialog, id ->
                val numInput = findViewById<TextView>(R.id.numInput)
                Log.d("eilon", numInput.text.toString())
                if (numInput.text.isNotEmpty()) {
                    viewModel?.addNewItem(RootItem(num = numInput.text.toString().toInt()))
                }
            }
            .setNegativeButton("CANCEL", null)
            .create()
    }
}