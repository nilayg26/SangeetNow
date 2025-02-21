package com.example.sangeetnow.ViewModel
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sangeetnow.DataClasses.MainData
import com.example.sangeetnow.createToastMessage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Streaming
class DataViewModel:ViewModel() {
     var currState by mutableStateOf<DataState>(Idle)
         private set
    private val _mainData= MutableLiveData(MainData(emptyList(), "", 0))
    val mainData : LiveData<MainData> = _mainData
    private val client= HttpClient(Android)
    var isLoading by mutableStateOf(false)
        private set
    var readyToDisplayMeditation by mutableStateOf(false)
        private set
    var readyToDisplaySpeaker by mutableStateOf(false)
        private set
    fun clearLogOut(){
        readyToDisplaySpeaker=false
    }
    fun getSongList(context: Context,search:String){
        currState=LoadingData
        isLoading=true
        if(!Build.checkNetwork(context)){
            context.createToastMessage("Turn on Wifi or Mobile data")
        }
        val data = Build.search(search)
        data.enqueue(object : Callback<MainData?> {
            override fun onResponse(p0: Call<MainData?>, p1: Response<MainData?>) {
                _mainData.value =
                    p1.body()
                isLoading=false
                currState=Idle
            }
            override fun onFailure(p0: Call<MainData?>, p1: Throwable) {
                println("Error is $p1")
                isLoading=false
                currState=Idle
            }
        }
        )
    }
    suspend fun getAnimation(url:String,sharedPreferences: SharedPreferences,name:String) {
        currState = LoadingData
        if (name == "meditation" && !readyToDisplayMeditation || name=="speaker" && !readyToDisplaySpeaker) {
            try {
                val responseServer = client.get(url).body<String>()
                val responseCached = sharedPreferences.getString(name, "") ?: ""
                if (responseServer != responseCached) {
                    sharedPreferences.edit()
                        .putString(name, responseServer)
                        .apply()
                }
                when (name) {
                    "meditation" -> {
                        readyToDisplayMeditation = true
                    }

                    else -> {
                        readyToDisplaySpeaker = true
                    }
                }
                currState = Retrieved
            } catch (e: Exception) {
                println("Hello " + e.message.toString())
                val jsonStr = sharedPreferences.getString(name, "") ?: ""
                if (jsonStr.isNotEmpty()) {
                    when (name) {
                        "meditation" -> {
                            readyToDisplayMeditation = true
                        }

                        else -> {
                            readyToDisplaySpeaker = true
                        }
                    }
                    currState = Retrieved
                } else {
                    currState = ErrorData(e.message.toString())
                }
            }
        }
    }
}
interface DataState{
    var value:String
}
object Idle:DataState{
    override var value: String="idle"
}
object LoadingData:DataState{
    override var value: String="isLoading"
}
object Retrieved:DataState{
    override var value: String="gotData"
}
data class ErrorData(var msg :String):DataState
{
    override var value: String = "Error"
}
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