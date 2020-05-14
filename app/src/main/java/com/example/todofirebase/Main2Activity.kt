package com.example.todofirebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    lateinit var title:String
    lateinit var details:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar5)

        val nm = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Simple Notification
//            nm.createNotificationChannel(NotificationChannel("first","default",NotificationManager.IMPORTANCE_DEFAULT))

            //Heads up notification
            val channel = (NotificationChannel("first","default",NotificationManager.IMPORTANCE_HIGH))
            channel.apply {
                enableLights(true)
                enableVibration(true)
            }
            nm.createNotificationChannel(channel)
        }

        val key = intent.getStringExtra("key")

        et1.setText(intent.getStringExtra("title"))
        et2.setText(intent.getStringExtra("details"))

        val dbRef = FirebaseDatabase.getInstance().reference  // this is the reference of the root node of the database
        val firebaseUser = FirebaseAuth.getInstance().currentUser!! // this is the reference of the user id authenticated
        save.setOnClickListener {
            if (key.isNullOrEmpty()){
                title = et1.text.toString()
                details = et2.text.toString()

                val id = dbRef.child(firebaseUser.uid).child("usernote").push().key.toString()

                val un = usernote(title,details,id)
                dbRef.child(firebaseUser.uid).child("usernote").child(id).setValue(un)


                val builder =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        Notification.Builder(this,"first")
                    } else{
                        Notification.Builder(this)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                    }

                val simpleNotification = NotificationCompat.Builder(this,"first")
                    .setContentTitle("Title: ${title}")
                    .setContentText("Your note has been created successfully")
                    .setSmallIcon(R.drawable.todo)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                nm.notify(1,simpleNotification)
            }
            else{
                title = et1.text.toString()
                details = et2.text.toString()

                dbRef.child(firebaseUser.uid).child("usernote").child(key).child("title").setValue(title)
                dbRef.child(firebaseUser.uid).child("usernote").child(key).child("details").setValue(details)

                val i = Intent(this,MainActivity::class.java).setPackage("com.example.todofirebase")

                val pi = PendingIntent.getActivity(this,123,i,PendingIntent.FLAG_UPDATE_CURRENT)

                val builder =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        Notification.Builder(this,"first")
                    } else{
                        Notification.Builder(this)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                    }

                val clickableNotification = NotificationCompat.Builder(this,"first")
                    .setContentTitle("Title: ${title}")
                    .setContentText("Your note has been updated successfully")
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.todo)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                nm.notify(2,clickableNotification)
            }

            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }

    }

}


