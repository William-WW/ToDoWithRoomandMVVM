package com.example.todolist.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.ui.NoteAdapter
import com.example.todolist.intent.Constants
import com.example.todolist.R
import com.example.todolist.db.note.Note
import com.example.todolist.viewmodel.NoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_isi.*

class MainActivity : AppCompatActivity(), NoteAdapter.NoteEvents {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var searchView: SearchView
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isiActivity.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(this)
        isiActivity.adapter = noteAdapter

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNotes()?.observe(this, Observer {
            noteAdapter.setNotes(it)
        })
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navView.selectedItemId = R.id.notes
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addmenu -> {
                    val intent = Intent(this@MainActivity, CreateActivity::class.java)
                    startActivityForResult(intent, Constants.INTENT_CREATE_NOTE)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val note = data?.getParcelableExtra<Note>(Constants.INTENT_OBJECT)!!
            when (requestCode) {
                Constants.INTENT_CREATE_NOTE -> {
                    noteViewModel.insertNote(note)
                }
                Constants.INTENT_UPDATE_NOTE -> {
                    noteViewModel.updateNote(note)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.searchnote)
            ?.actionView as SearchView
        searchView.setSearchableInfo(searchManager
            .getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                noteAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchnote -> true

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun resetListView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
    }

    override fun onDeleteClicked(notes: Note) {
        noteViewModel.deleteNote(notes)
    }

    override fun onBackPressed() {
        resetListView()
        super.onBackPressed()
    }

    override fun onViewClicked(notes: Note) {
        resetListView()
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        intent.putExtra(Constants.INTENT_OBJECT, notes)
        startActivityForResult(intent, Constants.INTENT_UPDATE_NOTE)
    }
}
