package com.example.sangeetnow
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.google.ai.client.generativeai.GenerativeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@Composable
fun RandomSong(navController: NavHostController, param: () -> Unit) {

    val mainData = remember { MutableLiveData(MainData(emptyList(), "", 0)) }
    val context= LocalContext.current
    if(!Build.checkNetwork(context)){
        context.createToastMessage("Turn on Wifi/Mobile Data")
        return
    }
    var response by rememberSaveable {
        mutableStateOf("")
    }
    var can by rememberSaveable {
        mutableStateOf(false)
    }
    var text by rememberSaveable {
        mutableStateOf("")
    }
    var clicked by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .padding(10.dp)
            .fillMaxWidth(),Arrangement.SpaceEvenly,Alignment.CenterVertically) {
        if (clicked) {
            LoadingScreen()
        }
       TextField(value =text , onValueChange ={it->text=it},Modifier.fillMaxWidth(0.65f), label = { Text(
           text = "Your Mood? Your tone...😌", fontSize = 10.sp
       )})
        Button(onClick = { clicked=true;println("Button called") }) {
            Text(text = if(clicked)"☑️" else "✅")
        }
    }
    LaunchedEffect(clicked){
        if(clicked) {
            response = getResponse(context,text)
        }
        val data = Build.search(response)
        data.enqueue(object : Callback<MainData?> {
            override fun onResponse(p0: Call<MainData?>, p1: Response<MainData?>) {
                mainData.value = p1.body()
                println(p1.body().toString())
                if(clicked){
                   can = true
                }
            }
            override fun onFailure(p0: Call<MainData?>, p1: Throwable) {
                println("Error is $p1")
                context.createToastMessage("Something went wrong")
                return
            }
        })
    }
    if (response.isNotEmpty() && can) {
        val songList = mainData.value?.data
        if (!songList.isNullOrEmpty()&&response!="safety") {
            CurrentMusic.data = songList[0]
            navController.navigate(Title.route)
        }
        else if(response == "safety"){
            context.createToastMessage("Sorry could not load")
        }
        else{
            context.createToastMessage("Song cannot be found")
        }
        can=false
        param()
        return
    }
}
suspend fun getResponse(context:Context,prompt:String):String{
    return try {
        println(prompt)
        val apiKey = BuildConfig.API_KEY
        val generativeModel = GenerativeModel(modelName = "gemini-1.5-flash", apiKey = apiKey)
        val response =
            generativeModel.generateContent(prompt = "Give me a $prompt song, it should be available on Deezer music api,your response should consist of only one song name, Don't give name of movie, Just the song name")
        println(prompt)
        println(response.text.toString())
        response.text.toString()
    }
    catch (t:Throwable){
        println("Safety was found")
        "safety"
    }
}