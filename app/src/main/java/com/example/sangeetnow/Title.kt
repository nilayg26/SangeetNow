package com.example.sangeetnow

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val mediaPlayer = remember {
        mutableStateOf(MediaPlayer())
    }
    var isPlaying by remember {
        mutableStateOf(false)
    }
    var isFirstTime by remember {
        mutableStateOf(false)
    }
    mediaPlayer.value.setDataSource(mContext, Uri.parse(CurrentMusic.data.preview))
    mediaPlayer.value.prepareAsync()
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
                                    if (isPlaying) {
                                        mediaPlayer.value.pause()
                                        isPlaying = false
                                    } else {
                                        MediaPlayers.checkOn(mediaPlayer.value)
                                        if (isFirstTime) {
                                            mediaPlayer.value.setOnPreparedListener {
                                                isPlaying = true
                                                mediaPlayer.value.start()
                                                isFirstTime = false
                                            }
                                        } else {
                                            mediaPlayer.value.start()
                                            isPlaying = true
                                        }
                                        mediaPlayer.value.setOnCompletionListener {
                                            isPlaying = false
                                        }
                                    }
                                } else {
                                    mContext.createToastMessage("Mobile Data/Wifi Off")
                                    isPlaying = false
                                    mediaPlayer.value.pause()
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
                        MusicPlayerScreen(mediaPlayer = mediaPlayer.value,isPlaying)
                    }
                    }
            }
        }
    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.value.isPlaying) {
                mediaPlayer.value.stop()  // Stop playback
                mediaPlayer.value.release()
            }
        }
    }

    }

@Composable
fun MusicPlayerScreen(mediaPlayer: MediaPlayer, isPlaying: Boolean) {
    var songProgress by remember { mutableFloatStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }
        Row(modifier = Modifier
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Slider(
                value = songProgress,
                onValueChange = { newValue ->
                    isSeeking = true
                    songProgress = newValue
                },
                onValueChangeFinished = {
                    isSeeking = false
                    mediaPlayer.seekTo((songProgress * mediaPlayer.duration).toInt())  // Seek to the new position
                },
                modifier = Modifier
                    .fillMaxWidth(0.7F)
                    .padding(5.dp)
            )
            Text(text = "${((songProgress * mediaPlayer.duration).toLong() / 1000)}s")
        }
        LaunchedEffect(isPlaying) {
               println("LaunchedEffectCalled")
                while (isPlaying) {
                    if (!isSeeking) {
                        songProgress = mediaPlayer.currentPosition.toFloat()/mediaPlayer.duration
                    }
                    delay(1000)
                }

        }
    }