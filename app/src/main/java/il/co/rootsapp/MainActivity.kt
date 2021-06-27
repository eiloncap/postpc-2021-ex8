package il.co.rootsapp;

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RootsAdapter
    private lateinit var rootsRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        rootsRecycleView = findViewById(R.id.rootsRecycleView)

        initAdapter()
        val alert = initDialog()

        rootsRecycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        addButton.setOnClickListener {
            alert.show()
        }

        RootsApp.instance.db.listLiveData.observe(this) { list -> adapter.setItems(list) }
    }


    private fun initAdapter() {
        adapter = RootsAdapter(RootsApp.instance.db::deleteItem, RootsApp.instance.db::cancelItem)
        rootsRecycleView.adapter = adapter
    }

    private fun initDialog(): Dialog {
        val v = layoutInflater.inflate(R.layout.input_alert, null)
        return AlertDialog.Builder(this)
            .setMessage("Enter a number to calculate:")
            .setCancelable(true)
            .setView(v)
            .setPositiveButton("OK") { _, _ ->
                val numInput = v.findViewById<TextView>(R.id.numInput)
                if (numInput.text.isNotEmpty()) {
                    val num = numInput.text.toString().toLong()
                    // todo: handle 0 and 1 input?
                    RootsApp.instance.db.addNewItem(num)
                    adapter.setItems(RootsApp.instance.db.items)
                    numInput.text = ""
                }
            }
            .setNegativeButton("CANCEL") { _, _ ->
                val numInput = v.findViewById<TextView>(R.id.numInput)
                numInput.text = ""
            }
            .create()
    }
}