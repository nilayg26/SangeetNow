package com.example.sangeetnow.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sangeetnow.R
import com.example.sangeetnow.createToastMessage
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel:ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<State>()
    val authState: LiveData<State> = _authState
    suspend fun login(sharedPreferences: SharedPreferences, context: Context) {
        _authState.value = Loading
        val request = getRequest(context = context)
        val credentialManager = CredentialManager.create(context = context)
        try {
            val result = credentialManager.getCredential(request = request, context = context)
            when (result.credential) {
                is CustomCredential -> {
                    if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
                            result.credential.data
                        )
                        val googleIdTokenId = googleIdTokenCredential.idToken
                        val authCredential = GoogleAuthProvider.getCredential(googleIdTokenId, null)
                        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val name= auth.currentUser?.displayName?:"No name can be found"
                                val email= auth.currentUser?.email?:"No email can be found"
                                val picUrl=auth.currentUser?.photoUrl?:"null"
                                sharedPreferences.edit()
                                    .putBoolean("loginStatus",true)
                                    .putString("name",name)
                                    .putString("email",email)
                                    .putString("picUrl",picUrl.toString())
                                    .apply()
                                _authState.value = Authenticated
                            } else {
                                val msg=task.exception?.message ?: "Something went wrong"
                                context.createToastMessage(msg)
                                _authState.value =Error(msg)
                            }
                        }
                    } else {
                        context.createToastMessage("Try Again!")
                        _authState.value=UnAuthenticated
                    }
                }
            }

        }
        catch (e:Exception) {
            context.createToastMessage("Could not get to your Google Account")
            println("Error from LogIn Function: "+e.message.toString())
            _authState.value= UnAuthenticated
        }
    }
        private fun getRequest(context: Context): GetCredentialRequest {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
            return (GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build())
        }
        fun logOut(
            context: Context,
            sharedPreferences: SharedPreferences,
            dataViewModel: DataViewModel
        ) {
            dataViewModel.clearLogOut()
            val jsonStr= sharedPreferences.getString("meditation","")?:""
            sharedPreferences.edit().clear()
                .putString("meditation",jsonStr)
                .apply()
            auth.signOut()
            _authState.value = UnAuthenticated
        }
}
interface State{
    var status:String
}
object Loading:State{
    override var status="loading"
}
object Authenticated:State{
    override var status="auth"
}
object UnAuthenticated:State{
    override var status="unauth"
}
data class Error(var msg:String):State{
    override var status: String="Error"
}