package com.example.sangeetnow
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeetnow.ui.theme.LightModeColors
import com.example.sangeetnow.ui.theme.SangeetNowTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Build.createBuilder("https://deezerdevs-deezer.p.rapidapi.com/")
            SangeetNowTheme {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(LightModeColors.Orange),
                ) {
                    Navigation()
                }
            }
        }
    }
}
@Composable
fun Navigation(){
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = WelcomePage.route){
        composable(WelcomePage.route){
            WelcomePage(navController)
        }
        composable(Search.route){
            SearchPage(navController)
        }
        composable(Title.route){
            Title(navController)
        }
    }
}