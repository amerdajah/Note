package com.amerdev.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amerdev.note.adapters.NotesAdapter
import com.amerdev.note.models.Note
import com.amerdev.note.persistence.NoteRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NotesAdapter.OnItemClickListener {

    private val notesList = ArrayList<Note>()
    private lateinit var adapter: NotesAdapter
    private lateinit var mNoteRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNoteRepository = NoteRepository(this)



        setSupportActionBar(findViewById(R.id.toolBar))
        title = "Notes"

        adapter = NotesAdapter(notesList, this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        retrieveNotes()
    }

    private fun retrieveNotes(){
        mNoteRepository.retrieveNotesTask()
            .observe(this@MainActivity, object : Observer,
                androidx.lifecycle.Observer<List<Note>> {
                override fun onChanged(t: List<Note>?) {
                    if (notesList.size > 0){
                        notesList.clear()
                    }
                    if (notesList.isEmpty()){
                        notesList.addAll(t!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun update(p0: Observable?, p1: Any?) {}
            })
    }

    fun insertNote(view: View){
        startActivity(Intent(this, ViewNoteActivity::class.java))
    }

    fun removeNote(note: Note){
        CoroutineScope(IO).launch {
            mNoteRepository.deleteNote(note)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val note = notesList[position]
        val intent = Intent(this, ViewNoteActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }

    private fun generateDummyList(size: Int): ArrayList<Note> {
        val list = ArrayList<Note>()
        for (i in 0 until size) {
            val item = Note(  "Line 2", "20$i", "2020")
            list += item
        }
        return list
    }

    private var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            removeNote(notesList[viewHolder.adapterPosition])
        }
    }
}