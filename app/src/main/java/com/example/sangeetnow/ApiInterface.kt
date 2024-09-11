package com.example.sangeetnow

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.http.UrlRequest.Status
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Streaming

interface ApiInterface {
    @Headers("x-rapidapi-key: 134cce9bd5msh2d80cbcac8f9c0fp1889b3jsne556ff0150ea","x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com")
    @Streaming
    @GET("search") //End Point
    fun getData(@Query("q") query: String): Call<MainData>
}
object Build{
    private lateinit var builder:ApiInterface
    private lateinit var baseUrl: String
    fun createBuilder(baseUrl:String) {
        this.baseUrl=baseUrl
        builder= Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
    }
    fun getBaseUrl():String{
        return baseUrl
    }
    fun search(str:String):Call<MainData>{
        return builder.getData(str)
    }
    fun checkNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val network = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}