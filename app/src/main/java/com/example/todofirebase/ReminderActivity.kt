package com.example.todofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reminder.*
import kotlinx.android.synthetic.main.activity_reminder.addnotes
import kotlinx.android.synthetic.main.activity_reminder.toolbar
import kotlinx.android.synthetic.main.item_list.*

class ReminderActivity : AppCompatActivity() {

    val dbRef = FirebaseDatabase.getInstance().reference
    val firebaseUser = FirebaseAuth.getInstance()

    val reminderlist = arrayListOf<reminders>()
    val reminderAdapter = ReminderAdapter(reminderlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        setSupportActionBar(toolbar)

        reminderRv.apply {
            layoutManager = LinearLayoutManager(this@ReminderActivity,RecyclerView.VERTICAL,false)
            adapter = reminderAdapter
        }
        reminderAdapter.onItemClick = {
            Toast.makeText(this,"${it.title} Clicked",Toast.LENGTH_SHORT).show()
        }
        addReminder.setOnClickListener {
            startActivity(Intent(this,SetreminderActivity::class.java))
        }

        retrieveData()

    }

    private fun retrieveData() {
        dbRef.child(firebaseUser.currentUser!!.uid).child("reminders").addChildEventListener(object :
            ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(reminders::class.java)!!
                reminderlist.add(data)
                reminderAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
//                time.visibility = View.VISIBLE
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(reminders::class.java)!!
                reminderlist.add(data)
                reminderAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
//                time.visibility = View.VISIBLE
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val data = p0.getValue(reminders::class.java)!!
                reminderlist.add(data)
                reminderAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
//                time.visibility = View.VISIBLE
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.remindermenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.note->{
                startActivity(Intent(this,MainActivity::class.java))
            }
            R.id.sign_out->{
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        // user is now signed out
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        super.onBackPressed()
    }

}
