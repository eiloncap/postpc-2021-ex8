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
    private lateinit var rootsRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = if (viewModel == null) viewModel else RootsApp.instance.viewModel

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        rootsRecycleView = findViewById(R.id.rootsRecycleView)

        val alert = initDialog()

        rootsRecycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        initAdapter()

        addButton.setOnClickListener {
            alert.show()
        }
    }

    private fun initAdapter() {
        adapter = RootsAdapter()
        rootsRecycleView.adapter = adapter
    }

    private fun initDialog(): Dialog {
        val v = layoutInflater.inflate(R.layout.input_alert, null)
        return AlertDialog.Builder(this)
            .setMessage("Enter a number to calculate:")
            .setCancelable(true)
            .setView(v)
            .setPositiveButton("OK") { dialog, id ->
                val numInput = v.findViewById<TextView>(R.id.numInput)
                Log.d("eilon", numInput.text.toString())
                if (numInput.text.isNotEmpty()) {
                    viewModel?.addNewItem(RootItem(num = numInput.text.toString().toInt()))
                }
            }
            .setNegativeButton("CANCEL", null)
            .create()
    }
}