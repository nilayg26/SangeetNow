package com.example.sangeetnow
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeetnow.Pages.AccountPage
import com.example.sangeetnow.Pages.LogIn
import com.example.sangeetnow.Pages.SearchPage
import com.example.sangeetnow.Pages.Title
import com.example.sangeetnow.Pages.WelcomePage
import com.example.sangeetnow.ViewModel.AuthViewModel
import com.example.sangeetnow.ui.theme.LightModeColors
import com.example.sangeetnow.ui.theme.SangeetNowTheme
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences=this.getSharedPreferences("SangeetNow", Context.MODE_PRIVATE)
        val authViewModel:AuthViewModel by viewModels()
        setContent {
            Build.createBuilder("https://deezerdevs-deezer.p.rapidapi.com/")
            SangeetNowTheme {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(LightModeColors.Orange),
                ) {
                    Navigation(sharedPreferences,authViewModel)
                }
            }
        }
    }
}
@Composable
fun Navigation(sharedPreferences: SharedPreferences, authViewModel: AuthViewModel) {
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination =WelcomePage.route){
        composable(WelcomePage.route){
            WelcomePage(navController,sharedPreferences)
        }
        composable(Search.route, enterTransition = { scaleIn(tween(500)) }){
            SearchPage(navController,sharedPreferences)
        }
        composable(Title.route, enterTransition = {slideInHorizontally()} ,
               exitTransition = {slideOutHorizontally()}
        ){
            Title(navController)
        }
        composable(AccountPage.route,enterTransition = { scaleIn()}, exitTransition = { scaleOut() }){
            AccountPage(navController,sharedPreferences,authViewModel)
        }
        composable(LoginPage.route, enterTransition = { scaleIn() }, exitTransition = { scaleOut() }){
           LogIn(navController,sharedPreferences,authViewModel)
        }
    }
}