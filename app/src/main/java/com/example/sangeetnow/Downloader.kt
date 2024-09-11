package com.example.sangeetnow

import java.net.URL

interface Downloader{
    fun downloadFile(url: String):Long
}