package com.example.sangeetnow
import android.media.MediaPlayer
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
import kotlinx.coroutines.delay

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
            modifier = Modifier.size(25.dp)
        )
    }

}
object TitlePlayer{
    var value:MediaPlayer=MediaPlayer()
    var first=true
    fun setSource(url: String){
       value.release()
        value = MediaPlayer()
        value.setDataSource(url)
       value.prepareAsync()
    }
}
object MyPlayer{
    var value:MediaPlayer=MediaPlayer()
    var first=true
    private var map= mutableMapOf<Int,()->(Unit)>()
    fun setSource(url: String){
        value.release()
        value= MediaPlayer()
        value.setDataSource(url)
        value.prepareAsync()
    }
    fun addToMap(i:Int,s:()->(Unit)){
        map.put(i,s)
    }
    fun pauseAll(i:Int){
        map.forEach{ (it, func) ->
            if(it!=i){
                func()
            }
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
                    MusicCard(title = it.title, picUrl = it.album.cover_small, song = it.preview,navController,it,i)
                }
            }
        }

    }
}
@Composable
fun MusicCard(
    title: String,
    picUrl: String,
    song: String,
    navController: NavHostController,
    data: Data,
    i: Int
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                MyPlayer.value.release()
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
            Player(url =song, i = i)
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
@Composable
fun Player(title: String="",url:String="",i: Int) {
    val mContext = LocalContext.current
    val firstTime= remember { mutableStateOf(true) }
    var isPlaying by remember{ mutableStateOf(false) }
    var click by remember{ mutableStateOf(false) }
    MyPlayer.addToMap(i){
        isPlaying = false
    }
    Row {
        if(firstTime.value&&click){
            LoadingScreen()
        }else{
        Button(colors = ButtonDefaults.buttonColors(LightModeColors.Yellow),onClick = {
            if (Build.checkNetwork(context = mContext)){
                if(isPlaying){
                    MyPlayer.value.pause()
                    isPlaying=false
                }
                else{
                    if(!MyPlayer.first){
                        MyPlayer.pauseAll(i)
                        MyPlayer.setSource(url)
                        MyPlayer.value.setOnPreparedListener {
                            MyPlayer.value.start()
                            firstTime.value=false
                            isPlaying = true
                            MyPlayer.first=false
                        }
                    }
                    if(firstTime.value){
                        click=true
                        MyPlayer.pauseAll(i)
                        MyPlayer.setSource(url)
                        MyPlayer.value.setOnPreparedListener {
                            MyPlayer.value.start()
                            firstTime.value=false
                            isPlaying = true
                            MyPlayer.first=false
                        }
                    }
                    else{
                        MyPlayer.pauseAll(i)
                        MyPlayer.value.start()
                        isPlaying = true
                    }
                    MyPlayer.value.setOnCompletionListener {
                        isPlaying=false
                    }
                }
            }
            else{
                mContext.createToastMessage("Mobile Data/Wifi Off")
                isPlaying=false
                MyPlayer.value.pause()
            }}) {
            Text(text = if (isPlaying) "⏸️" else "▶️", color = LightModeColors.Blue)
        }
        }
    }

    DisposableEffect(Unit){
        onDispose {
                MyPlayer.value.release()
        }
    }
}