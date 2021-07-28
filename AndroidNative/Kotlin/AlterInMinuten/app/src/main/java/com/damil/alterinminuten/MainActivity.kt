package com.damil.alterinminuten

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnDatePicker.setOnClickListener {view->
            onClickDatePicker(view)
        }



    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onClickDatePicker(view: View){

        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            //Toast.makeText(this, "Geburtsdatum: " + "$dayOfMonth.$month.$year", Toast.LENGTH_LONG).show()

            var selectedDate = "$dayOfMonth.${month+1}.$year"
            tvSelectedDate.setText(selectedDate)

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
            val sdfDate = sdf.parse(selectedDate)

            val selectedDateToMinutes = sdfDate!!.time / 60000

            val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
            val currentDateToMinutes = currentDate!!.time / 60000

            val differenceInMinutes = currentDateToMinutes - selectedDateToMinutes

            tvSelectedDateInMinutes.setText(differenceInMinutes.toString())


        }, year, month, day)


        dpd.datePicker.maxDate = Date().time - 3*86400000
        dpd.show()

    }
}