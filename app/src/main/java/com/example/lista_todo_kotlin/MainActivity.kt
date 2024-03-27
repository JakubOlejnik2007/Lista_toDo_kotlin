package com.example.lista_todo_kotlin

import SharedPreferencesManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAdd: Button
    private lateinit var buttonReset: Button
    private lateinit var poleEdycyjne: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private lateinit var manager: SharedPreferencesManager
    private var lista = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = SharedPreferencesManager(this)

        buttonAdd = findViewById(R.id.buttonAdd)
        buttonReset = findViewById(R.id.buttonReset)
        recyclerView = findViewById(R.id.recyclerView)
        poleEdycyjne = findViewById(R.id.editText)
        lista = manager.getList("lista")

        adapter = TodoAdapter(lista)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonAdd.setOnClickListener { handleAddElementButtonOnClick(it) }
        buttonReset.setOnClickListener { handleResetButtonOnClick(it) }



        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                lista.removeAt(position)
                adapter.notifyDataSetChanged()
                manager.saveList("lista", lista)
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun handleAddElementButtonOnClick(view: View) {
        val poleEdycyjneValue = poleEdycyjne.text.toString().trim()
        if (poleEdycyjneValue.isEmpty()) return
        else {
            lista.add(poleEdycyjneValue)
            manager.saveList("lista", lista)
            adapter.notifyDataSetChanged()
            clearEditText()
        }
    }

    private fun handleResetButtonOnClick(view: View) {
        lista.clear()
        manager.saveList("lista", lista)
        adapter.notifyDataSetChanged()
    }

    private fun clearEditText() {
        poleEdycyjne.setText("")
    }

    class TodoAdapter(private val dataSet: MutableList<String>) :
        RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(android.R.id.text1).text = dataSet[position]
        }

        override fun getItemCount() = dataSet.size
    }
}
