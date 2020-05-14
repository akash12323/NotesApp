package com.example.todofirebase

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_setreminder.*
import java.text.SimpleDateFormat
import java.util.*

class SetreminderActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar:Calendar

    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener

    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var date = 0L
    var time = 0L
    lateinit var title:String
    lateinit var details:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setreminder)

        setSupportActionBar(toolbar5)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)

        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val dbRef = FirebaseDatabase.getInstance().reference

        save_reminder.setOnClickListener {
            val id = dbRef.child(firebaseUser.uid).child("reminders").push().key.toString()

            title = et1.text.toString()
            details = et2.text.toString()

            val r = reminders(title,details,date,time,id)

            dbRef.child(firebaseUser.uid).child("reminders").child(id).setValue(r)

            val i = Intent(this,ReminderActivity::class.java)
            val pi = PendingIntent.getActivity(this,System.currentTimeMillis().toInt(),i,PendingIntent.FLAG_ONE_SHOT)

            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            Log.d("ABC","${Calendar.getInstance().time}")
            alarmManager.set(AlarmManager.RTC_WAKEUP,time,pi)

            startActivity(i)
        }

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.dateEdt->{
                setListener()
            }
            R.id.timeEdt -> {
                setTimeListener()
            }
        }
    }
    private fun setListener(){
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker, year:Int, month:Int, dayOfMonth:Int->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate(){
        val myFormat = "EEE , d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        date = myCalendar.time.time
        dateEdt.setText(sdf.format(myCalendar.time))

        timeInpLay.visibility = View.VISIBLE
    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }
    private fun updateTime() {
        //Mon, 5 Jan 2020
        val myformat = "hh:mm a"
        val sdf = SimpleDateFormat(myformat)
        time = myCalendar.time.time
        timeEdt.setText(sdf.format(myCalendar.time))
    }
}
