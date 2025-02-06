package com.example.sangeetnow.Download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.example.sangeetnow.CurrentMusic
import com.example.sangeetnow.helperFunction

class AndroidDownloader(context:Context) : Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(url: String): Long {
        val request=DownloadManager.Request(url.toUri()).setMimeType("audio/mpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("${helperFunction(CurrentMusic.data.title,"",10)}.mp3")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${
                helperFunction(
                    CurrentMusic.data.title,"",10)
            }.mp3")
        return  downloadManager.enqueue(request)
    }
}