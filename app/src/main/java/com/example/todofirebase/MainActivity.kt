package com.example.todofirebase

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


const val RC_SIGN_IN = 1000

class MainActivity : AppCompatActivity() {

    val dbRef = FirebaseDatabase.getInstance().reference

    val notesList = arrayListOf<usernote>()
    val todoAdapter = TodoAdapter(notesList)

    val firebaseUser = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        databaseRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
            adapter = todoAdapter
        }
        todoAdapter.onItemClick = {
            val i = Intent(this,Main3Activity::class.java)
            i.putExtra("title",it.title.toString())
            i.putExtra("details",it.details.toString())
            i.putExtra("key",it.id.toString())
            Toast.makeText(this,"${it.title}",Toast.LENGTH_SHORT).show()
            startActivity(i)
        }

        addBtn.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
        }

        if (firebaseUser.currentUser != null){
            //Logged In
            retrieveData()
        }
        else{
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(
                        Arrays.asList(
                            GoogleBuilder().build(),
                            PhoneBuilder().build()
                        )
                    )
                    .build(),
                RC_SIGN_IN
            )
        }

    }

    private fun retrieveData() {

        dbRef.child(firebaseUser.currentUser!!.uid).child("usernote").addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(usernote::class.java)!!
                notesList.add(data)
                todoAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(usernote::class.java)!!
                notesList.add(data)
                todoAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val data = p0.getValue(usernote::class.java)!!
                notesList.add(data)
                todoAdapter.notifyDataSetChanged()
                addnotes.visibility = View.GONE
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.searchmenu,menu)

        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView
        searchView.setQueryHint("type here to search")

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                displayTodo(newText.toString())
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signout->{
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        // user is now signed out
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
            }
            R.id.reminder->{
                startActivity(Intent(this,ReminderActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayTodo(newText: String) {
        val newList = arrayListOf<usernote>()

        for (`object` in notesList) {
            if (`object`.title.toLowerCase().contains(newText.toLowerCase())) {
                newList.add(`object`)
                val adapternew = TodoAdapter(newList)
                databaseRv.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity,RecyclerView.VERTICAL,false)
                    adapter = adapternew
                }
            }

        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                retrieveData()
            } else { // Sign in failed
                if (response == null) { // User pressed back button
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    return
                }
            }
        }
    }

    override fun onBackPressed() {
//        AlertDialog.Builder(this)
//            .setTitle("Are you sure")
//            .setMessage("Do you want to exit!!")
//            .setPositiveButton("YES",{ dialogInterface: DialogInterface, i: Int -> finish() })
//            .setNegativeButton("NO",{ dialogInterface: DialogInterface, i: Int -> })
//            .show()
        super.onBackPressed()
    }


}