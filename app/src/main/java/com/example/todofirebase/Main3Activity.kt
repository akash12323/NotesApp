package com.example.todofirebase

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    lateinit var details:String
    lateinit var title:String
    lateinit var key:String

    var clicked = false
    val dbRef = FirebaseDatabase.getInstance().reference

    val firebaseUser = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        setSupportActionBar(toolbar3)

        details = intent.getStringExtra("details")
        title = intent.getStringExtra("title")
        key = intent.getStringExtra("key")

        textView1.text = title
        textView2.text = details

        update.setOnClickListener {
            val i = Intent(this,Main2Activity::class.java)
            i.putExtra("title",title.toString())
            i.putExtra("details",details.toString())
            i.putExtra("key",key.toString())
            startActivity(i)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deletemenu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete->{
                AlertDialog.Builder(this)
                    .setTitle("Are You Sure")
                    .setMessage("Do you want to delete the note")
                    .setPositiveButton("YES",{ dialogInterface: DialogInterface, i: Int ->
                        dbRef.child(firebaseUser.currentUser!!.uid).child("usernote").child(key).setValue(null)
                        startActivity(Intent(this,MainActivity::class.java))
                    })
                    .setNegativeButton("NO",{ dialogInterface: DialogInterface, i: Int -> })
                    .show()
//                if (clicked == false){
//                    Toast.makeText(this,"Press again to  delete",Toast.LENGTH_SHORT).show()
//                    clicked = true
//                }
//                else{
//                    dbRef.child(firebaseUser.currentUser!!.uid).child("usernote").child(key).setValue(null)
//                    startActivity(Intent(this,MainActivity::class.java))
//                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
