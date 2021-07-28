package com.damil.ageinminutes

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var tvSelectedDate:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)
        tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)



        btnDatePicker.setOnClickListener {view ->
            clickDatePicker(view)

        }




    }
    fun clickDatePicker(view:View){
        val myCalendar = Calendar.getInstance();
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->
            Toast.makeText(applicationContext, "The chosen year is $year, the month is $selectedMonth and the day is $day", Toast.LENGTH_LONG).show();

            val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"

            tvSelectedDate.setText(selectedDate)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val theDate = sdf.parse(selectedDate)


        }, year, month, day).show()

    }


}