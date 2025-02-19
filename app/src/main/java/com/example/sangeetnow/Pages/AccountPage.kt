package com.example.sangeetnow.Pages
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.sangeetnow.LoginPage
import com.example.sangeetnow.R
import com.example.sangeetnow.Search
import com.example.sangeetnow.TextCardSN
import com.example.sangeetnow.TextFieldSN
import com.example.sangeetnow.ViewModel.AuthViewModel
import com.example.sangeetnow.ViewModel.UnAuthenticated
import com.example.sangeetnow.ui.theme.LightModeColors

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountPage(
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    authViewModel: AuthViewModel
) {
    val context= LocalContext.current
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(LightModeColors.YellowD)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val authState=authViewModel.authState.observeAsState()
        var email by remember {
            mutableStateOf(sharedPreferences.getString("email",""))
        }
        var name by remember {
            mutableStateOf(sharedPreferences.getString("name",""))
        }
        var picUrl by remember {
            mutableStateOf(sharedPreferences.getString("picUrl",""))
        }
        LaunchedEffect(authState.value) {
            when(authState.value){
                UnAuthenticated->{
                    navController.navigate(LoginPage.route){
                        popUpTo(Search.route){
                            inclusive=true
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        TextCardSN("Your Details!")
        Spacer(modifier = Modifier.height(60.dp))
        GlideImage(model = picUrl, failure = placeholder(R.drawable.appicon)
            , contentDescription = "", modifier = Modifier.clip(RoundedCornerShape(20.dp)).size(100.dp))
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldSN(enable = false,password = false,
            text = name.toString(), lamda = {it}, label = "Name"
        )
        TextFieldSN(enable = false,password = false,
            text = email.toString(), lamda = {it}, label = "Email"
        )
        Spacer(modifier = Modifier.height(60.dp))
        Button(onClick = {
            authViewModel.logOut(context,sharedPreferences)
        }, colors = ButtonDefaults.buttonColors(containerColor = LightModeColors.Orange2, contentColor = Color.White)) {
            Text(text = "Log Out ", fontSize = 18.sp)
        }
    }
}