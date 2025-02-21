package com.example.sangeetnow
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeetnow.Pages.AccountPage
import com.example.sangeetnow.Pages.LogIn
import com.example.sangeetnow.Pages.SearchPage
import com.example.sangeetnow.Pages.Title
import com.example.sangeetnow.Pages.WelcomePage
import com.example.sangeetnow.ViewModel.AuthViewModel
import com.example.sangeetnow.ViewModel.Build
import com.example.sangeetnow.ViewModel.DataViewModel
import com.example.sangeetnow.ui.theme.LightModeColors
import com.example.sangeetnow.ui.theme.SangeetNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(scrim = LightModeColors.YellowD.toArgb(),
                darkScrim = LightModeColors.YellowD.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(scrim = LightModeColors.YellowD.toArgb(),
                darkScrim = LightModeColors.YellowD.toArgb()
            )
        )
        val sharedPreferences=this.getSharedPreferences("SangeetNow", MODE_PRIVATE)
        val authViewModel:AuthViewModel by viewModels()
        val dataViewModel by viewModels<DataViewModel> ()
        setContent {
            Build.createBuilder("https://deezerdevs-deezer.p.rapidapi.com/")
            SangeetNowTheme {
                Scaffold {innerPadding->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(LightModeColors.YellowD),
                    ) {
                        Navigation(sharedPreferences,authViewModel,dataViewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun Navigation(
    sharedPreferences: SharedPreferences,
    authViewModel: AuthViewModel,
    dataViewModel: DataViewModel
) {
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination =WelcomePage.route){
        composable(WelcomePage.route){
            WelcomePage(navController,sharedPreferences)
        }
        composable(Search.route, enterTransition = { scaleIn(tween(500)) }){
            SearchPage(navController,sharedPreferences,dataViewModel)
        }
        composable(Title.route, enterTransition = {slideInHorizontally(
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )} ,
               exitTransition = {slideOutHorizontally(animationSpec = tween(500))}){
            Title(navController)
        }
        composable(AccountPage.route,enterTransition = { scaleIn()}, exitTransition = { scaleOut() }){
            AccountPage(navController,sharedPreferences,authViewModel,dataViewModel)
        }
        composable(LoginPage.route, enterTransition = { scaleIn() }, exitTransition = { scaleOut() }){
           LogIn(navController,sharedPreferences,authViewModel,dataViewModel)
        }
    }
}