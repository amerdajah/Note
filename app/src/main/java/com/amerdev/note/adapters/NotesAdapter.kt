package com.amerdev.note.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amerdev.note.R
import com.amerdev.note.models.Note
import com.amerdev.note.util.Utility
import kotlinx.android.synthetic.main.layout_note_list_item.view.*

class NotesAdapter(private val notesList: List<Note>,
                   private val listener: OnItemClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_note_list_item,
            parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notesList[position]
        holder.title.text = currentNote.title
        var month = currentNote.timeStamp.substring(0, 2)
        month = Utility.getMonthFromNumber(month)
        val year = currentNote.timeStamp.substring(3)
        val time = "$month $year"
        holder.timeStamp.text = time
    }

    override fun getItemCount(): Int = notesList.size

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val title: TextView = itemView.note_title
        val timeStamp: TextView = itemView.note_timeStamp

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}