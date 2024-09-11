package com.example.sangeetnow
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.sangeetnow.ui.theme.LightModeColors

@Composable
fun AppButton(str: String,onClick:()->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 80.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(15.dp),
        colors = CardDefaults.cardColors(containerColor = LightModeColors.Orange)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text =str,Modifier.padding(10.dp), fontWeight = FontWeight.Bold,color = LightModeColors.Blue )
        }

    }
}
@Composable
fun DisplaySongs(mainData: MainData, navController: NavHostController) {
    val state= rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        if (mainData.data != null) {
            mainData.data.forEachIndexed {i,it->
                if(i<10) {
                    MusicCard(title = it.title, picUrl = it.album.cover_small, song = it.preview,navController,it)
                }
            }
        }

    }
}
@Composable
fun MusicCard(title:String,picUrl:String,song:String,navController: NavHostController,data: Data){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                MediaPlayers.pauseAndOff()
                CurrentMusic.data = data
                navController.navigate(Title.route)
            },
        elevation = CardDefaults.elevatedCardElevation(15.dp),
        colors = CardDefaults.cardColors(containerColor = LightModeColors.Orange),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = helperFunction(title), Modifier.padding(10.dp), fontWeight = FontWeight.Bold,color = LightModeColors.Blue)
            Spacer(modifier = Modifier.weight(0.2F))
            Player(url =song)
            Image(
                painter = rememberAsyncImagePainter(model = picUrl),
                contentDescription = "album photo",
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

    }
}
fun helperFunction(str:String,extra:String="",i:Int=13):String{
    val full=str+extra
    if(full.length>i){
        return full.substring(0,i)+"..."
    }
    return str
}
object CurrentMusic{
    lateinit var data:Data
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(0.2F),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 5.dp,
            modifier = Modifier.size(35.dp)
        )
    }

}
object MediaPlayers{
   val list= mutableMapOf<MediaPlayer,(Boolean)->Unit>()
    fun checkOn(mediaPlayer: MediaPlayer) {
        list.forEach { (player, setIsPlaying) ->
            if (mediaPlayer != player && player.duration != -1 && player.isPlaying) {
                player.pause()
                setIsPlaying(false)
            }
        }
    }
    fun pauseAndOff(){
        if(list.isNotEmpty()) {
            list.forEach { (player, off) ->
                player.pause()
                off(false)
            }
        }
    }
}
@Composable
fun Player(title: String="",url:String="") {
    val mContext = LocalContext.current
    val firstTime= remember { mutableStateOf(true) }
    val mediaPlayer = remember {
        mutableStateOf(MediaPlayer())
    }
    var isPlaying by remember{ mutableStateOf(false) }
    MediaPlayers.list[mediaPlayer.value] = { playing -> isPlaying = playing }
    if(mediaPlayer.value.duration!=-1) {
        isPlaying = mediaPlayer.value.isPlaying
    }
    Row {
        Button(colors = ButtonDefaults.buttonColors(LightModeColors.Yellow),onClick = {
            if (Build.checkNetwork(context = mContext)){
            if(mediaPlayer.value.duration==-1) {
                mediaPlayer.value.setDataSource(mContext, Uri.parse(url))
                mediaPlayer.value.prepareAsync()
            }
                if(isPlaying){
                        mediaPlayer.value.pause()
                        isPlaying=false
                }
                else{
                    MediaPlayers.checkOn(mediaPlayer.value)
                    if(firstTime.value){
                        isPlaying = true
                    mediaPlayer.value.setOnPreparedListener {
                        mediaPlayer.value.start()
                        firstTime.value=false
                    }
                    }
                    else{
                        mediaPlayer.value.start()
                        isPlaying = true
                    }
                    mediaPlayer.value.setOnCompletionListener {
                        isPlaying=false
                    }
                }
        }
        else{
            mContext.createToastMessage("Mobile Data/Wifi Off")
                isPlaying=false
                mediaPlayer.value.pause()
        }}) {
            Text(text = if (isPlaying) "⏸️" else "▶️", color = LightModeColors.Blue)
        }
    }
    DisposableEffect(Unit){
        onDispose {
            if(isPlaying){
                mediaPlayer.value.stop()
                mediaPlayer.value.release()
            }
        }
    }
}
