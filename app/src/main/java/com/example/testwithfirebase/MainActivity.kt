package com.example.testwithfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        val ref= FirebaseDatabase.getInstance().getReference("person")
        ref.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("data", dataSnapshot.child("name").value.toString())
                ref.child("name").setValue("Boss")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("=== 0nCeancel ==", "Failed to read value.", error.toException())
            }
        })
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w( "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
                Log.e("== msg == ", token)
            })
        val personAdd = PersonModel(4444, "Adum","maybeK",22)
        ref.setValue(personAdd)
    }
}
