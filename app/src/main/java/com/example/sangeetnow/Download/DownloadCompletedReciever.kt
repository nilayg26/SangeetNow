package com.example.sangeetnow.Download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.sangeetnow.createToastMessage

class DownloadCompletedReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action=="android.intent.action.DOWNLOAD_COMPLETE"){
            val id=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L)
            if(id!=-1L){
                context?.createToastMessage("Download Completed")
            }
        }
    }
}