package com.example.sangeetnow.Pages

import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sangeetnow.LoginPage
import com.example.sangeetnow.R
import com.example.sangeetnow.Search
import com.example.sangeetnow.WelcomePage
import com.example.sangeetnow.ui.theme.LightModeColors
import kotlinx.coroutines.delay
import okhttp3.internal.wait

@Composable
fun WelcomePage(navController: NavHostController, sharedPreferences: SharedPreferences) {
    val loginStatus=sharedPreferences.getBoolean("loginStatus",false)
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(0)
        visible = true
        delay(1700)
        navController.navigate(if(!loginStatus){LoginPage.route} else{Search.route}){
            popUpTo(WelcomePage.route){
                inclusive=true
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(LightModeColors.YellowD), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = visible, enter = fadeIn(tween(2000))) {
            Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.appicon), contentDescription ="app icon", modifier = Modifier.padding(bottom = 20.dp).clip(
                    RoundedCornerShape(40.dp)))
                Text(text = "Sangeet Now: The Preview App", fontStyle = FontStyle.Italic, fontSize = 22.sp, color = LightModeColors.Blue, fontWeight = FontWeight.Bold,modifier = Modifier.padding(20.dp))
                Text(text = "AI on Board 👽",  fontSize = 28.sp, color = LightModeColors.Blue, fontWeight = FontWeight.Bold,modifier = Modifier.padding(bottom = 20.dp))
            }

        }
    }
}