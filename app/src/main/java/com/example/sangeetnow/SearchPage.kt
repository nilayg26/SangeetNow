package com.example.sangeetnow

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.sangeetnow.ui.theme.LightModeColors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SearchPage(navController: NavHostController) {
    val context= LocalContext.current
    var isClicked by rememberSaveable() {
        mutableStateOf(false)
    }
        val mainData = remember { MutableLiveData(MainData(emptyList(), "", 0)) }
        val dataChanged = mainData.observeAsState(initial = MainData(emptyList(), "", 0))
        var search by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(LightModeColors.YellowD)
        ) {

            Text(
                text = "Search 🔎",
                Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
                color = LightModeColors.Blue,
                fontSize = 40.sp
            )
            OutlinedTextField(
                value = search, onValueChange = { search = it;isClicked=false }, modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                label = { Text(text = "Type to Search 🎵 🎶 👨🏻‍🎤 ...") }
            )
            if (search == "") {
                Spacer(modifier = Modifier.weight(1F))
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AppButton(str = "Click for AI 👽🎧") {
                        isClicked=true
                    }
                    if(isClicked){
                        if(Build.checkNetwork(context)) {
                            RandomSong(navController = navController) { isClicked = false }
                        }
                        else{
                            context.createToastMessage("Turn On Wifi/Mobile Data")
                            isClicked=false
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.75f))
                    Text(text = "App By Nilay", fontStyle = FontStyle.Italic, fontSize = 12.sp, color = LightModeColors.Blue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 7.dp))
                }
            } else {
                LaunchedEffect(search) {
                    if(!Build.checkNetwork(context)){
                        context.createToastMessage("Turn on Wifi or Mobile data")
                    }
                    val data = Build.search(search)
                    data.enqueue(object : Callback<MainData?> {
                        override fun onResponse(p0: Call<MainData?>, p1: Response<MainData?>) {
                            mainData.value =
                                p1.body()
                        }
                        override fun onFailure(p0: Call<MainData?>, p1: Throwable) {
                            println("Error is $p1")
                        }
                    }
                    )
                }

                DisplaySongs(
                    mainData = dataChanged.value,
                    navController
                )

            }
        }

    }