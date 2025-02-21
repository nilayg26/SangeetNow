package com.example.sangeetnow.Pages
import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sangeetnow.AnimationLottie
import com.example.sangeetnow.LoadingScreen
import com.example.sangeetnow.LoginPage
import com.example.sangeetnow.R
import com.example.sangeetnow.Search
import com.example.sangeetnow.TextCardSN
import com.example.sangeetnow.ViewModel.AuthViewModel
import com.example.sangeetnow.ViewModel.Authenticated
import com.example.sangeetnow.ViewModel.DataViewModel
import com.example.sangeetnow.ViewModel.Loading
import com.example.sangeetnow.ui.theme.LightModeColors
import kotlinx.coroutines.launch

@SuppressLint("ResourceType")
@Composable
fun LogIn(
    navController: NavHostController,
    sharedPreferences: SharedPreferences,
    authViewModel: AuthViewModel,
    dataViewModel: DataViewModel
) {
    val verticalScroll= rememberScrollState()
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val authState = authViewModel.authState.observeAsState()
        var isLoading by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(Unit) {
            dataViewModel.getAnimation(context.getString(R.string.meditation), sharedPreferences = sharedPreferences,name="meditation")
        }
        LaunchedEffect(authState.value) {
            when (authState.value) {
                Authenticated -> {
                    navController.navigate(Search.route) {
                        popUpTo(LoginPage.route) {
                            inclusive = true
                        }
                    }
                }
                Loading -> {
                    isLoading = true
                }

                else -> {
                    isLoading = false
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .background(LightModeColors.YellowD)
                .verticalScroll(verticalScroll)
                ,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Spacer(modifier = Modifier.height(60.dp))
            TextCardSN("Lets Get you in!", color = LightModeColors.Orange2)
            Spacer(modifier = Modifier.height(80.dp))
            AnimatedVisibility(dataViewModel.readyToDisplayMeditation) {
                val jsonStr= sharedPreferences.getString("meditation","") ?: ""
                AnimationLottie(jsonStr =jsonStr)
            }
            Spacer(modifier = Modifier.height(80.dp))
            Button(
                modifier = Modifier.animateContentSize(),
                onClick = {
                    coroutineScope.launch {
                        authViewModel.login(sharedPreferences, context)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightModeColors.Orange2,
                    contentColor = Color.White
                )
            ) {
                    if (!isLoading) {
                        Text(text = "Continue with ", fontSize = 18.sp)
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(20.dp).clip(
                                RoundedCornerShape(10.dp)
                            )
                        )
                    } else {
                        LoadingScreen()
                    }
            }
        }

}
