package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.db.note.Note
import com.example.todolist.R
import com.example.todolist.ui.MainActivity
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(noteEvents: NoteEvents) : RecyclerView.Adapter<NoteAdapter.TodoViewHolder>(),
    Filterable {

    private var notes: List<Note> = arrayListOf()
    private var filterednote: List<Note> = arrayListOf()
    private val listener: NoteEvents = noteEvents

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
        holder.bind(filterednote[position], listener as MainActivity)
        val sortedList = filterednote.sortedWith(
                    if(MainActivity.isSortByDateCreated) {
                            compareBy({it.waktubuat}, {it.waktuupdate})
                    }
                    else {
                            compareBy({ it.tempo }, { it.waktutempo })
                    })
                holder.bind(sortedList[position], listener)
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(notes: Note, listener: MainActivity) {
            val parsedWaktuBuat = SimpleDateFormat("dd/MM/yy", Locale.JAPAN).parse(notes.waktubuat) as Date
            val waktubuat = formatDate(parsedWaktuBuat, "dd MMM yyyy")

            val parsedWaktuUpdate = SimpleDateFormat("dd/MM/yy", Locale.JAPAN).parse(notes.waktuupdate) as Date
            val waktuupdate = "Diupdate: " + formatDate(parsedWaktuUpdate, "dd MMM yyyy")

            itemView.waktu_buat.text = waktubuat
            itemView.waktu_update.text = waktuupdate
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

        private fun formatDate(date: Date, format: String): String{
            return date.toString(format)
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
                        if (row.title.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(
                                Locale.ROOT
                            )
                            )
                            || row.note.contains(charString.toLowerCase(Locale.ROOT))
                        ) {
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

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

