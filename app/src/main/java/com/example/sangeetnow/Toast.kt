package com.example.sangeetnow

import android.content.Context
import android.widget.Toast

fun Context.createToastMessage(text:String,duration:Int=0){
    if(duration==0) { //0 for Short Duration
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    else{ //any other val for Long Duration
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}