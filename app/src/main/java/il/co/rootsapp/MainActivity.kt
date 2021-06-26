package il.co.rootsapp;

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import il.co.rootsapp.R

class MainActivity : AppCompatActivity() {

//    var holder: TodoItemsHolder? = null
    private lateinit var adapter: RootsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                TODO("add a worker and update view")
            }
            .setNegativeButton("CANCEL", null)
            .create()
    }
}