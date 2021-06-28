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
    lateinit var rootsRecycleView: RecyclerView
        private set
    private lateinit var app: RootsApp
    lateinit var db: RootsDB
        private set
    lateinit var alert: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        app = application as RootsApp
        db = app.db

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        rootsRecycleView = findViewById(R.id.rootsRecycleView)
        rootsRecycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter = RootsAdapter(db::deleteItem, db::cancelItem)
        rootsRecycleView.adapter = adapter
        adapter.setItems(db.listLiveData.value)

        alert = initDialog()

        addButton.setOnClickListener { alert.show() }
        db.listLiveData.observe(this) { list -> adapter.setItems(list) }
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
                    db.addNewItem(num)
                    numInput.text = ""
                }
            }
            .setNegativeButton("CANCEL") { _, _ ->
                val numInput = v.findViewById<TextView>(R.id.numInput)
                numInput.text = ""
            }
            .create()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.setItems(db.listLiveData.value)

    }
}