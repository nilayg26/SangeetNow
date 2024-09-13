package com.example.sangeetnow

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.sangeetnow.ui.theme.LightModeColors
import kotlinx.coroutines.delay

@Composable
fun Title(navController: NavHostController) {
    val mContext= LocalContext.current
    val downloader=AndroidDownloader(mContext)
    var isPlaying by remember {
        mutableStateOf(false)
    }
    var isFirstTime by remember {
        mutableStateOf(true)
    }
    if (isFirstTime) {
        TitlePlayer.value.release()
        TitlePlayer.setSource(CurrentMusic.data.preview)
        TitlePlayer.value.setOnPreparedListener {
            isFirstTime = false
        }
    }
        Card(modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(LightModeColors.Yellow), colors = CardDefaults.cardColors(LightModeColors.Yellow)) {
            Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = rememberAsyncImagePainter(model = CurrentMusic.data.album.cover_big), contentDescription = "icon",
                    modifier = Modifier
                        .padding(end = 20.dp, start = 20.dp, top = 20.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .aspectRatio(1f)
                )
                Card(modifier = Modifier
                    .padding(end = 10.dp, start = 10.dp, bottom = 20.dp)
                    .fillMaxSize(),colors = CardDefaults.cardColors(LightModeColors.Orange), elevation =  CardDefaults.elevatedCardElevation(15.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                            Text(text = "🎶 "+helperFunction(CurrentMusic.data.title,i=CurrentMusic.data.title.length) ,
                                Modifier.padding(20.dp),
                                fontWeight = FontWeight.Bold,
                                color = LightModeColors.Blue,
                                fontSize = 25.sp)
                            Text(text ="👨🏻‍🎤 "+helperFunction(CurrentMusic.data.artist.name,i=30) ,
                                Modifier.padding(10.dp),
                                fontWeight = FontWeight.Bold,
                                color = LightModeColors.Blue,
                                fontSize = 20.sp)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = {
                                if (Build.checkNetwork(context = mContext)) {
                                    if(!isFirstTime){
                                    if (isPlaying) {
                                        TitlePlayer.value.pause()
                                        isPlaying = false
                                    } else {
                                        TitlePlayer.value.start()
                                        isPlaying = true
                                    }
                                    TitlePlayer.value.setOnCompletionListener {
                                        isPlaying = false

                                    }
                                }
                                } else {
                                    mContext.createToastMessage("Mobile Data/Wifi Off")
                                    isPlaying = false
                                    TitlePlayer.value.pause()
                                }
                            }, colors = ButtonDefaults.buttonColors(LightModeColors.Yellow)) {
                                Text(
                                    text = if (isPlaying) "⏸️" else "▶️",
                                    color = LightModeColors.Blue,
                                    fontSize = 30.sp
                                )
                            }
                            Button(onClick = {downloader.downloadFile(CurrentMusic.data.preview)}, colors = ButtonDefaults.buttonColors(LightModeColors.Yellow)) {
                                Text(
                                    text = "⬇️",
                                    color = LightModeColors.Blue,
                                    fontSize = 30.sp
                                )
                            }
                        }
                        if (!isFirstTime) {
                            MusicPlayerScreen(mediaPlayer = TitlePlayer.value,isPlaying)
                        }
                        else{
                            LoadingScreen()
                        }
                    }
                    }
            }
        }
    DisposableEffect(Unit) {
        onDispose {
            TitlePlayer.value.release()
            TitlePlayer.first=true
            MyPlayer.first=true
        }
    }

    }

@Composable
fun MusicPlayerScreen(mediaPlayer: MediaPlayer, isPlaying: Boolean) {
    var songProgress by remember { mutableFloatStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Slider(
            valueRange = 0f..100f,
            value = songProgress,  // Directly use songProgress
            onValueChange = { newValue ->
                isSeeking = true
                songProgress = newValue
            },
            onValueChangeFinished = {
                isSeeking = false
                mediaPlayer.seekTo((songProgress / 100 * mediaPlayer.duration).toInt())  // Seek to the new position
            },
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .padding(5.dp)
        )
            Text(text = "${((songProgress / 100 * mediaPlayer.duration) / 1000).toLong()}s")
    }

    // Launch effect to update song progress while playing
    LaunchedEffect(mediaPlayer.isPlaying) {
        while (mediaPlayer.isPlaying) {
            if (!isSeeking) {
                songProgress = (mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration) * 100  // Normalize progress
            }
            delay(1000)
        }
    }
}
