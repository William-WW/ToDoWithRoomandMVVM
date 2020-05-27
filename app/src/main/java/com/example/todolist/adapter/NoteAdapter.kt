package com.example.note.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.db.note.Note
import com.example.todolist.R
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(todoEvents: NoteEvents) : RecyclerView.Adapter<NoteAdapter.TodoViewHolder>(),
    Filterable {

    private var notes: List<Note> = arrayListOf()
    private var filterednote: List<Note> = arrayListOf()
    private val listener: NoteEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,
            parent, false)
        return TodoViewHolder(view)
    }

    fun setNotes(note: List<Note>) {
        this.notes = note
        this.filterednote = note
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = filterednote.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(filterednote[position], listener)
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(notes: Note, listener: NoteEvents) {
            itemView.waktu_buat.text = notes.waktubuat
            itemView.title_note.text = notes.title
            itemView.isi_note.text = notes.note
            itemView.tanggal_jatuh.text = notes.tempo
            itemView.jatuh_tempo.text = notes.waktutempo

            itemView.delete.setOnClickListener {
                listener.onDeleteClicked(notes)
            }
            itemView.setOnClickListener {
                listener.onViewClicked(notes)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filterednote = if (charString.isEmpty()) {
                    notes
                } else {
                    val filteredList = arrayListOf<Note>()
                    for (row in notes) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())
                            || row.note.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filterednote
                return filterResults
            }
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterednote = p1?.values as List<Note>
                notifyDataSetChanged()
            }

        }
    }

    interface NoteEvents {
        fun onDeleteClicked(notes: Note)
        fun onViewClicked(notes: Note)
    }
}