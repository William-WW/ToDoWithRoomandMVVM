package com.example.todolist.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.db.note.Note
import com.example.todolist.R
import com.example.todolist.intent.Constants
import kotlinx.android.synthetic.main.activity_create.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {

    private var note: Note? = null
    private var bulan: Int = 0
    private var hari: Int = 0
    private var tahun: Int = 0
    private var jam: Int = 0
    private var menit: Int = 0
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val intent = intent
        if (intent != null && intent.hasExtra(Constants.INTENT_OBJECT)) {
            val note: Note = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.note = note
        }

        tanggal_tempo.setOnClickListener {
            showDatePickerDialog()
        }

        waktu_tempo.setOnClickListener {
            showTimePickerDialog()
        }

        title = if (note != null) getString(R.string.edit) else getString(R.string.create_note)
    }

    private fun saveNote(){
        val sdf = SimpleDateFormat("dd/M/yy HH:mm")
        val id = if (note != null) note?.id else null
        val note = Note(id = id,
            title = editTitle.text.toString(),
            note = editIsi.text.toString(),
            tempo = tanggal_tempo.text.toString(),
            waktutempo = waktu_tempo.text.toString(),
            waktubuat = sdf.format(Date()),
            waktuupdate = sdf.format(Date())
        )
        val intent = Intent()
        intent.putExtra(Constants.INTENT_OBJECT, note)
        setResult(RESULT_OK, intent)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate = menuInflater
        menuInflate.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_note -> {
                saveNote()
            }
        }
        return true
    }


    private fun showDatePickerDialog() {
        hari = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        bulan = Calendar.getInstance().get(Calendar.MONTH)
        tahun = Calendar.getInstance().get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, tahun_muncul, bulan_muncul, hari_muncul ->
                tanggal_tempo.text = ("" + hari_muncul + "/" + (bulan_muncul+1) + "/" + tahun_muncul)
                hari = hari_muncul
                bulan = bulan_muncul
                tahun = tahun_muncul
            },
            tahun,
            bulan,
            hari
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        jam = calendar.get(Calendar.HOUR_OF_DAY)
        menit = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, jam, menit ->
            calendar.set(Calendar.HOUR_OF_DAY, jam)
            calendar.set(Calendar.MINUTE, menit)
            waktu_tempo.text = SimpleDateFormat("HH:mm").format(calendar.time)
        }
        TimePickerDialog(this, timePickerDialog, jam, menit,true).show()
    }
}
