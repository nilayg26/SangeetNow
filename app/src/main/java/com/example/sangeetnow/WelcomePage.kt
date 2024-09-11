package com.example.sangeetnow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sangeetnow.ui.theme.LightModeColors
import kotlinx.coroutines.delay
@Composable
fun WelcomePage(navController: NavHostController) {
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(0)
        visible = true
        delay(2000)
        navController.navigate(Search.route)
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