package com.example.sangeetnow.Pages

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sangeetnow.AccountPage
import com.example.sangeetnow.AnimationLottie
import com.example.sangeetnow.AppButton
import com.example.sangeetnow.DisplaySongs
import com.example.sangeetnow.IconButtonSN
import com.example.sangeetnow.R
import com.example.sangeetnow.ViewModel.Build
import com.example.sangeetnow.ViewModel.DataViewModel
import com.example.sangeetnow.createToastMessage
import com.example.sangeetnow.ui.theme.LightModeColors

@Composable
fun SearchPage(
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    dataViewModel: DataViewModel
) {
    val savedSearch=sharedPreferences.getString("search","") ?:""
    val context= LocalContext.current
    var isClicked by rememberSaveable() {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        dataViewModel.getAnimation(context.getString(R.string.speaker),sharedPreferences,"speaker")
    }
    val scrollState= rememberScrollState()
        val dataChanged = dataViewModel.mainData.observeAsState()
        var search by rememberSaveable { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(LightModeColors.YellowD)
        ) {
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Search 🔎",
                    Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold,
                    color = LightModeColors.Blue,
                    fontSize = 40.sp
                )
                    IconButtonSN(sharedPreferences) {
                        navController.navigate(AccountPage.route)
                    }

            }

            OutlinedTextField(
                value = search, onValueChange = { search = it;isClicked=false }, modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                label = { Text(text = "Type to Search 🎵 🎶 👨🏻‍🎤 ...", color = Color.Black) },
                colors = OutlinedTextFieldDefaults.colors(Color.Black)
            )
            if (search == "") {
                Spacer(modifier = Modifier.weight(1F))
                Column(
                    Modifier.fillMaxSize().animateContentSize().verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    AppButton(str = "Click for AI 👽🎧") {
                        isClicked=true
                    }
                    AnimatedVisibility(isClicked){
                        if(Build.checkNetwork(context)) {
                            RandomSong(navController = navController) { isClicked = false }
                        }
                        else{
                            context.createToastMessage("Turn On Wifi/Mobile Data")
                            isClicked=false
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    AnimatedVisibility(dataViewModel.readyToDisplaySpeaker) {
                        val jsonStr=sharedPreferences.getString("speaker","") ?: ""
                        AnimationLottie(jsonStr = jsonStr)
                    }
                    Text(text = "App By Nilay", fontStyle = FontStyle.Italic, fontSize = 12.sp, color = LightModeColors.Blue, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 7.dp))
                }
            } else {
                LaunchedEffect(search) {
                    if (savedSearch != search) {
                        dataViewModel.getSongList(context = context, search = search)
                    }
                }
                AnimatedVisibility(!dataViewModel.isLoading && dataChanged.value!=null) {
                    DisplaySongs(
                        mainData = dataChanged.value,
                        navController
                    )
                }
                DisposableEffect(Unit) {
                    onDispose {
                        sharedPreferences.edit().putString("search",search).apply()
                    }
                }
            }
        }

    }